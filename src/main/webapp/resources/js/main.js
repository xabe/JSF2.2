var isVisible = false;
var clickedAway = false;

var isVisibleDownload = false;
var clickedAwayDownload = false;

/* funciones que se realizar mas carga la pagina */

$(document).ready(function() {

	$('input#text').val("");
	
	$('#form\\:text').focus();

	var content = $('#userDiv').attr("id");
	if(content != undefined)
	{
		$('#popover').popover({
			placement : 'bottom',
			animation : true,
			trigger : 'manual',
			html : true,
			content: $('#userDiv').html()
		}).click(function(e) {
	        $(this).popover('show');
	        clickedAway = false,
	        isVisible = true,
	        e.preventDefault();
	    });		
	}
	
	content = $('#downloadDiv').attr("id");
	if(content != undefined)
	{
		$('#form\\:popoverDownload').popover({
			placement : 'bottom',
			animation : true,
			trigger : 'manual',
			html : true,
			content: $('#downloadDiv').html()
		}).click(function(e) {
			$(this).popover('show');
			clickedAwayDownload = false,
			isVisibleDownload = true,
			e.preventDefault();
		});
	}
});


/* Funcion para evitar el cierre del popover de gestion de usuario */

$(document).click(function(e) {
	  if(isVisible & clickedAway)
	  {
	    $('#popover').popover('hide');
	    isVisible = clickedAway = false;
	  }
	  else
	  {
	    clickedAway = true;
	  }
});

/* Funcion para evitar el cierre del popover de descarga */

$(document).click(function(e) {
	  if(isVisibleDownload & clickedAwayDownload)
	  {
	    $('#form\\:popoverDownload').popover('hide');
	    isVisibleDownload = clickedAwayDownload = false;
	  }
	  else
	  {
		  clickedAwayDownload = true;
	  }
});


/* Funcion para activar el menu */

function activeMenu(id){
	$("#menu\\:"+id).parent().addClass('active');	
}

/* Funcion de mostar la modal de pedticiones Ajax */

function start() {
	statusDialog.show();
}

function stop() {
	statusDialog.hide();
}

/* Funciones de JAX-RS  */

function viewRestService(URL){
	getData(URL, 'xml', '#xml');
	getData(URL, 'json', '#json');
	getData(URL+'/jsonp', 'jsonp', '#jsonp');
}

function getData(URL,type,idResult){
	$.ajax({
		url: URL,
		type: 'GET',
		dataType: type,
		data: $(this).serialize(),
		success: function(data) {
			show_response(data, $(idResult), type);
			restModal.show();
		},
		error: function(data) {
			show_response(data, $(idResult), type);
			restModal.show();
		}
		
	});
}


function show_response (data, result, type) {
	result.removeClass();
	if(type == 'xml')
	{
		var xmlstr = data.xml ? data.xml : (new XMLSerializer()).serializeToString(data);
		result.text(vkbeautify.xml(xmlstr));
	}
	if(type == 'json')
	{
		result.text(vkbeautify.json(data));
	}
	if(type == 'jsonp')
	{
		result.text(vkbeautify.json(data));
	}
	result.addClass("prettyprint");
	prettyPrint();
}	