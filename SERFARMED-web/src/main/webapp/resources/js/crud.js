//jQuery.ready(function() {

  var handleSubmit = function(args, dialog) {
    var pos = dialog.indexOf("Dialog"); 
    var idDialog = dialog.substring(0,pos);
    idDialog = idDialog + "Dlg";
    
    var jqDialog = $('#' + idDialog);
    if (args.validationFailed) {
      jqDialog.effect('shake', {times: 3}, 100);
    } else {
      PF(dialog).hide();
    }
  }
  
  $("#datosForm\\:fechaMensual_input").change(function () {
    var fechaMensual = $("#datosForm\\:fechaMensual").val();
    $("#datosForm\\:fechaMensual_input").val(fechaMensual.substring(2,9));
  });
  
  
  var fechaMensual = function() {
    var fechaMensual = $("#datosForm\\:fechaMensual").val();
    $("#datosForm\\:fechaMensual_input").val(fechaMensual.substring(2,9));
  }
  
  
//});