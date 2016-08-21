$(document).ready(function () {

  function readURL(input) {
    if (input.files && input.files[0]) {
      var reader = new FileReader();

      reader.onload = function (e) {
        //$('#j_idt85:blah').attr('src', e.target.result);
//        $('#formId\\:blah').attr('src', e.target.result);
        $('.MostrarImagen')
                .attr('src', e.target.result)
      }

      reader.readAsDataURL(input.files[0]);
    }
  }
  
  $("#PacienteCreateEditForm\\:CargarImagen_input").change(function () {
    readURL(this);
  });
  
  
  $("#UpdateUserForm\\:CargarImagen_input").change(function () {
    readURL(this);
  });
  
});


  