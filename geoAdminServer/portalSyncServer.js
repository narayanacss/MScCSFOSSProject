
var MySQLEvents = require('mysql-events');
var geoUpdateWallet = require('config/geoUpdateWallet');
var geoUpdateMobile = require('config/geoUpdateMobile');
var geoDeleteMobile = require('config/geoDeleteMobile');

var dsn = {
  host:     'localhost',
  user:     '', //Include value
  password: '', //Include value
};
var MySQLEventWatcher = MySQLEvents(dsn);
var walletWatcher = MySQLEventWatcher.add(
  'geodrupal.geoWallet_balance',
  function (oldRow, newRow) {

    //row inserted
    if (oldRow === null) {
      console.log("geoWallet opening topup.");
      var uid = newRow.fields.uid;
      console.log("geoWallet opened for userid " + uid);
      geoUpdateWallet.geoUpdateWallet(uid,function (found) {
         console.log(found);
      });
    }

    //row updated
    if (oldRow !== null && newRow !== null) {
      var uid = newRow.fields.uid;
      console.log("geoWallet updated for userid " + uid);
      geoUpdateWallet.geoUpdateWallet(uid,function (found) {
         console.log(found);
      });
    }
  }
);


var mobileWatcher = MySQLEventWatcher.add(
  'geodrupal.twilio_user',
  function (oldRow, newRow) {

    //row inserted
    if (oldRow === null) {
      console.log("New mobile number. Confirmation awaited.");
    }

    //row deleted
    if (newRow === null) {
      var uid = oldRow.fields.uid;
      console.log("Mobile number deleted for userid " + uid);
      geoDeleteMobile.geoDeleteMobile(uid,function (found) {
         console.log(found);
      });
    }

    //row updated
    if (oldRow !== null && newRow !== null) {
      var uid = newRow.fields.uid;
      console.log("Mobile number updated for userid " + uid);
      geoUpdateMobile.geoUpdateMobile(uid,function (found) {
      console.log(found);
      });
    }
  }
);

console.log("portalSyncServer running.");

