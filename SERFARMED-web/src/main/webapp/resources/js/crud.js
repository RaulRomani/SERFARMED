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
  
//});