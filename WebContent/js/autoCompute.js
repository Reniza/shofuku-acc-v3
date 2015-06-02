function computeVat(){
	var totalSales= document.getElementById("invoice.totalSales").value;
	var vattedAmount= document.getElementById("invoice.totalAmount").value;
	var isVatSelected = document.getElementById('isVat').value;
	var vatAmount =0;
	
	//remove non numbers
	stripNonNumeric(totalSales);
	
	if(isVatSelected=='0'){
		
		//check decimal
	    numsplit = totalSales.split('.');
		  if (2 < numsplit.length) {
		    	alert("NumberFormatException: invalid format, formats should have no more than 1 period");
		    	 throw('NumberFormatException');
		    }
		  
	}else{
		
		var hasComma = -1 < totalSales.indexOf(',');

		if(hasComma){
			//remove decimal
		    numsplit = totalSales.split('.');
		    if (2 < numsplit.length) {
		    	alert("NumberFormatException: invalid format, formats should have no more than 1 period");
		    	 throw('NumberFormatException');
		    }
		    
		    //remove commas
		    numWithOutPrecision = numsplit[0].split(',');
		    fnum = numWithOutPrecision.join(''); 
		    
		    
		   
		    vattedAmount = parseFloat(fnum / 1.12).toFixed(2);
			vatAmount = parseFloat(vattedAmount).toFixed(2) * .12;
		  
			vatAmount = putCommas(vatAmount);
			vattedAmount = putCommas(vattedAmount);
		   

		
		}else{
						
			//alert(vatAmount);
			vattedAmount = parseFloat(totalSales / 1.12).toFixed(2);
			vatAmount = parseFloat(totalSales).toFixed(2) - parseFloat(vattedAmount).toFixed(2);
		}
	}
	document.getElementById("invoice.vat").value=vatAmount;
	document.getElementById("invoice.totalAmount").value=vattedAmount;
	
}

function putCommas(numToPutComma){
	
	numToPutComma = numToPutComma+'';
	fnumSplit = numToPutComma.split('.');
    
    
	 var cnum = fnumSplit[0],
     parr = [],
     j = cnum.length,
     m = Math.floor(j / 3),
     n = cnum.length % 3 || 3; // n cannot be ZERO or causes infinite loop 

   // break the number into chunks of 3 digits; first chunk may be less than 3
   for (var i = 0; i < j; i += n) {
     if (i != 0) {n = 3;}
     parr[parr.length] = cnum.substr(i, n);
     m -= 1;
   } 
   
   // put chunks back together, separated by comma
   finalNumber = parr.join(','); 

   // add the precision back in
   if (fnumSplit[1]) {finalNumber += '.' + fnumSplit[1];}
   
   return finalNumber;
   
}

//This function removes non-numeric characters
function stripNonNumeric( str )
{
  str += '';
  var rgx = /^\d|\.|-$/;
  var out = '';
  for( var i = 0; i < str.length; i++ )
  {
    if( rgx.test( str.charAt(i) ) ){
      if( !( ( str.charAt(i) == '.' && out.indexOf( '.' ) != -1 ) ||
             ( str.charAt(i) == '-' && out.length != 0 ) ) ){
        out += str.charAt(i);
      }
    }
  }
  return out;
}
