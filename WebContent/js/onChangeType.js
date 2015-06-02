function onTypeChangeSupplier() {
	 document.getElementById("clicked").value = "false";
	 
	  document.forms["searchForm"].action = 'searchSupplierAction.action';
	  document.forms["searchForm"].submit();	
	  
}
function onTypeChangeModuleParameter() {
	document.getElementById("clicked").value = "false";
	
	  document.forms["searchForm"].action = 'searchSupplierAction.action';
	  document.forms["searchForm"].submit();	
	  
}
 
function onTypeChangeDisbursement(formKo) {
	document.getElementById("clicked").value = "false";
	
	  document.forms[formKo].action = 'searchDisbursementAction.action';
	  document.forms[formKo].submit();	  
	  document.getElementById("moduleParameterValue").value = '';
}

function onTypeChangeCustomer() {
	  document.getElementById("clicked").value = "false";
	  
	  document.forms["searchForm"].action = 'searchCustomerAction.action';
	  document.forms["searchForm"].submit();
	  
}

function onTypeChangeReceipt() {
	  document.getElementById("clicked").value = "false";
	 
	  document.forms["searchForm"].action = 'searchReceiptAction.action';
	  document.forms["searchForm"].submit();
	 
}
function onTypeChangeInventory() {
	  document.getElementById("clicked").value = "false";
	 
	  document.forms["searchForm"].action = 'searchInventoryAction.action';
	  document.forms["searchForm"].submit();
	
}

function onTypeChangeFinancials() {
	  document.getElementById("clicked").value = "false";
	 
	  document.forms["searchForm"].action = 'searchFinancialsAction.action';
	  document.forms["searchForm"].submit();
	
}

function onTypeChangeSecurity() {
	  document.getElementById("clicked").value = "false";
	 
	  document.forms["searchForm"].action = 'searchSecurityAction.action';
	  document.forms["searchForm"].submit();
	
}

function clearParameterValue(){
	 document.getElementById("moduleParameterValue").value = '';
}


function showSearchByDate(){
	
	var searchByDate=document.getElementById("searchByDateTbl");
	var searchByStatus=document.getElementById("searchByStatusTbl");
	searchByDate.style.display = 'none';
	searchByStatus.style.display = 'none';
	
	if (document.getElementById("inventoryModule").value == 'FinishedProductTransferSlip' || document.getElementById("inventoryModule").value == 'OrderRequisition' 
		|| document.getElementById("inventoryModule").value == 'ReturnSlip'){
		searchByDate.style.display = 'block';
		searchByStatus.style.display = 'none';
		
	}else if (document.getElementById("inventoryModule").value == 'FinishedGoods' || document.getElementById("inventoryModule").value == 'RawMaterials' 
		|| document.getElementById("inventoryModule").value == 'TradedItems' || document.getElementById("inventoryModule").value == 'Utensils'
			|| document.getElementById("inventoryModule").value == 'OfficeSupplies'){
			searchByStatus.style.display = 'block';
			searchByDate.style.display = 'none';
	}
}

function loadItemSubClassification(thisForm){
	var classification = document.getElementById("classif").value;
	document.getElementById("tempClassif").value = classification;
	
	document.forms[thisForm].action = 'loadItemSubClassificationAction.action';
	  document.forms[thisForm].submit();
}

function computeVat(){
	var checkVat = document.getElementById("checkVat").checked;
	var amount = document.getElementById("amount").value;
	var vatableAmount = 0;
	var vatAmount = 0;
	
	if (checkVat==true){
		document.getElementById("vattableAmount").value = 0;
		document.getElementById("vatAmount").value = 0;
	}else{
		vatableAmount = amount.replace(/\,/g,"") / 1.12;
		vatAmount = vatableAmount * 0.12;
		
		document.getElementById("vattableAmount").value = roundPrice(vatableAmount);
		document.getElementById("vatAmount").value = roundPrice(vatAmount);
	}
}


	function roundPrice(num) {
	    var p = num.toFixed(2).split(".");
	    return p[0].split("").reverse().reduce(function(acc, num, i, orig) {
	        return  num + (i && !(i % 4) ? "," : "") + acc;
	    }, "") + "." + p[1];
	}
