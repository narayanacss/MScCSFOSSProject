<?php

/**
 * @file
 * geoWallet user account related functionality
 */

/**
 * Implements hook_user_categories().
 */
function geoWallet_user_categories() {
  return array(
    array(
      'name' => 'geoWallet',
      'title' => t('geoWallet'),
      'weight' => 3,
    ),
  );
}

/**
 * Implements hook_user_load().
 */
function geoWallet_user_load($users) {
  // Load data from the {geoWallet_balance} table for the user account.
  $result = db_select('geoWallet_balance', 'u')
    ->fields('u', array('uid', 'bal'))
    ->condition('uid', array_keys($users), 'IN')
    ->execute();
  foreach ($result as $record) {
    if (!empty($record->uid)) {
      // Assign the data to the user object.
      $users[$record->uid]->geoWallet_balance = (array) $record;
    }
  }
}

/**
 * Implements hook_user_insert().
 */
function geoWallet_user_insert(&$edit, $account, $category) {
  geoWallet_user_save($edit, $account, $category);
}

/**
 * Implements hook_user_update().
 */
function geoWallet_user_update(&$edit, $account, $category) {
  geoWallet_user_save($edit, $account, $category);
}


/**
 * Saves mobile number data to the {geoWallet_user} table in the database.
 */
function geoWallet_user_save(&$edit, $account, $category) {
  if (($category == 'geoWallet') && isset($edit['geoWallet'])) {
    $number = (object) $edit['geoWallet'];
    $number->uid = $account->uid;

    $primary_keys = array();
    if (isset($account->geoWallet_balance['bal'])) {
        $primary_keys = array('uid');
    }
    drupal_write_record('geoWallet_balance', $number, $primary_keys);

    $edit['geoWallet']['bal'] = NULL;
  }
}

/**
 * Implements hook_user_login().
 */
function geoWallet_user_login(&$edit, $account) {
  // If the users/ geoWallet has insufficient balance, let them know they
  // need to link to their settings page for top-up.
  if (geoWallet_edit_access($account) && (empty($account->geoWallet_balance) || $account->geoWallet_balance['bal'] < 1000)) {
    $account_link = l(t("account settings page"), 'user/' . $account->uid . '/edit/geoWallet');
    drupal_set_message(t("You must top-up your geoWallet. Go to the !link to top-up.", array('!link' => $account_link)), 'warning');
  }
}
