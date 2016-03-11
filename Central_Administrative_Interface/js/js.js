// JavaScript Document

function validate(){

	var password = document.forms["loginOperator"]["password"].value;;

	if (password != "gaura123"){
		    alert ("Invalid credentials!");
			return false;
	}
	else return true;
}


