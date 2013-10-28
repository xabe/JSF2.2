/* Funcion para hacer go to top*/

function toTop(){
	 $('html, body').animate({
	        scrollTop: $("#top").offset().top - 20
	    }, 500);
	 return false;
}

/* Funcion para poner el over sobre la tabla */

$("#form\\:tableData_data").on('mouseover','tr', function() {
	$(this).removeClass("even-row");
	$(this).addClass("ui-state-highlight");
});

$("#form\\:tableData_data").on('mouseout','tr', function() {
	$(this).removeClass("ui-state-highlight");
	$(this).parent().find('tr.ui-datatable-odd').addClass("even-row");
});

/* Funcion de cambiar la imagen de boton de editar un elemento de la tabla */

$("body").on('mouseover','.info',function() {
	$(this).removeClass("info_out");
	$(this).addClass("info_over");
});	

$("body").on('mouseout','.info',function() {
	$(this).removeClass("info_over");
	$(this).addClass("info_out");
});	

/* Funcion de cambiar la imagen de boton de editar un elemento de la tabla */

$("body").on('mouseover','.edit',function() {
	$(this).removeClass("edit_out");
	$(this).addClass("edit_over");
});	

$("body").on('mouseout','.edit',function() {
	$(this).removeClass("edit_over");
	$(this).addClass("edit_out");
});	

/* Funcion de cambiar la imagen de boton de editar un elemento de la tabla */

$("body").on('mouseover','.delete',function() {
	$(this).removeClass("delete_out");
	$(this).addClass("delete_over");
});	

$("body").on('mouseout','.delete',function() {
	$(this).removeClass("delete_over");
	$(this).addClass("delete_out");
});	

/* Funcion de cambiar la imagen de boton de editar un elemento de la tabla */

$("body").on('mouseover','.password',function() {
	$(this).removeClass("password_out");
	$(this).addClass("password_over");
});	

$("body").on('mouseout','.password',function() {
	$(this).removeClass("password_over");
	$(this).addClass("password_out");
});	

/* Funcion de cambiar la imagen de boton de editar grupo de un elemento de la tabla */

$("body").on('mouseover','.editGroup',function() {
	$(this).removeClass("editGroup_out");
	$(this).addClass("editGroup_over");
});	

$("body").on('mouseout','.editGroup',function() {
	$(this).removeClass("editGroup_over");
	$(this).addClass("editGroup_out");
});	

/* Funcion de cambiar la imagen de boton de editar grupo de un elemento de la tabla */

$("body").on('mouseover','.editPermission',function() {
	$(this).removeClass("editPermission_out");
	$(this).addClass("editPermission_over");
});	

$("body").on('mouseout','.editPermission',function() {
	$(this).removeClass("editPermission_over");
	$(this).addClass("editPermission_out");
});	