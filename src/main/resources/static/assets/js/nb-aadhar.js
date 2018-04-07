var $$ = function(selector) {
    return document.querySelectorAll(selector);
}, $$byId = function(idSelector){
    return document.getElementById(idSelector);
}

var toggleform = function(currentForm){
    switch(currentForm){
        case 'login':
        	$("#login-form").addClass('hidden');
        	$("#signup-form").removeClass('hidden');
            break;
        case 'signup':
        	$("#login-form").removeClass('hidden');
        	$("#signup-form").addClass('hidden');
            break;
    }
}

var login = function() {
    $("#bg-intro").addClass("hidden");
    $("#dashboard").removeClass("hidden");
    $Router.go("register");
}

var logout = function() {
    $("#bg-intro").removeClass("hidden");
    $("#dashboard").addClass("hidden");
}

$Router.config([
    {path:'register',templateUrl:'partial/register.html'},
    {path:'reportMissing',templateUrl:'partial/reportMissing.html'},
    {path:'track',templateUrl:'partial/track.html'},
    {otherwise:'register'}
],{
    defaultLinkClass:"card",
    activateLinks: true,
    afterRouteChange: function(){
        $('.closeIt').on('click',function(){
            $(this).parent().addClass('hidden');
        });
        $("input[need-otp]").on('blur',function(){
        	if($(this).val().length > 0 && ($(this).val().length > 12 || $(this).val().length < 12)){
        		$(this).addClass("error");
        		$(".message-holder .errorMsg .content").text("The Aadhar should be exactly 12 digit long");
        		$(".message-holder .errorMsg").removeClass("hidden");
        	} else if($(this).val().length > 0 && !$(this).val().match(/^[0-9]+$/)){
        		$(this).addClass("error");
        		$(".message-holder .errorMsg .content").text("The Aadhar can only be numbers");
        		$(".message-holder .errorMsg").removeClass("hidden");
        	} else {
        		$(this).removeClass("error");
        		$(".message-holder .errorMsg").addClass("hidden");
        		if($(this).val().length > 0){
        			$.ajax({
            	        url: '/BabyIdentification/api/otp/send',
            	        data: {"aadharId": $(this).val()},
            	        beforeSend: function(){showLoaderScreen(true)},
            	        type: 'GET',
            	        success: function(data){
            	            showLoaderScreen(false);
            	            /*$('#nb_aadharId')[0].innerHTML = data.uuid;
            	            $("#successfullMsg").removeClass("hidden");
            	            $("label").each(function(){
            	                $(this)[0].innerHTML = $(this).attr("default-content");
            	            });
            	            $("#newRegistryForm")[0].reset();*/
            	            console.log(data);
            	        }
            	    });
        		}
        	}
            //console.log($(this),$(this).position().left,$(this).position().top,$(this).height());
        });
        $('input[type="file"]').on('change', function(e){
            var fileName = '';
		    if(this.files)
                fileName = e.target.value.split( '\\' ).pop();
            var idxDot = fileName.lastIndexOf(".") + 1;
            var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
            if(fileName.trim()){
                if (extFile=="jpg" || extFile=="jpeg"){
                    this.nextElementSibling.innerHTML = fileName;
                    $(this.nextElementSibling).addClass('files-added');
                }else{
                    alert("Only jpg/jpeg files are allowed!");
                }
            }
        });
    }
});

var navigateTo = function(hashToGo){
    $Router.go(hashToGo);
}

var showLoaderScreen = function(cond){
    if(cond){
        $(".loader-screen").removeClass("hidden");
    } else {
        $(".loader-screen").addClass("hidden");
    }
}

var submitNewRegistry = function(){
    var fd = new FormData($("#newRegistryForm")[0]);
    $.ajax({
        url: '/BabyIdentification/api/upload/files',
        data: fd,
        beforeSend: function(){showLoaderScreen(true)},
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data){
            showLoaderScreen(false);
            $('#nb_aadharId')[0].innerHTML = data.uuid;
            $("#successfullMsg").removeClass("hidden");
            $("label").each(function(){
                $(this)[0].innerHTML = $(this).attr("default-content");
            });
            $("#newRegistryForm")[0].reset();
        }
    });
}

var submitMissingCase = function(){
    var fd = new FormData($("#missingReportForm")[0]);
    $.ajax({
        url: '/BabyIdentification/api/upload/missing',
        data: fd,
        beforeSend: function(){showLoaderScreen(true)},
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data){
            showLoaderScreen(false);
            $("#missingMsg").removeClass("hidden");
            $("#missingReportForm")[0].reset();
        }
    });
}

function formulateResultTable(data){
    var tbody = $("#missingKidsTable").find('tbody');
    for(var i =0 , j=1; i<data.length; i++){
        if(data[i] != null && data[i].uuid != null && data[i].score !=null){
            var row = $('<tr></tr>'),
            serialNo = $('<td></td>').text(j),
            uuid = $('<td></td>').text(data[i].uuid),
            matchPercentage = $('<td></td>').text(Math.round(data[i].score)+"%"),
            actionButton = $("<button class='action-btn'></button>").text("Contact Parents"),
            actionCall = $('<td></td>').append(actionButton);
            row.prepend(serialNo, uuid, matchPercentage, actionCall);
            tbody.append(row);
            j++;
        }
    }
}

var trackBaby = function(){
    var fd = new FormData($("#trackForm")[0]);
    $.ajax({
        url: '/BabyIdentification/api/retrieve/match',
        data: fd,
        beforeSend: function(){showLoaderScreen(true)},
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data){
            showLoaderScreen(false);
            $(".result").removeClass("hidden");
            formulateResultTable(data);
            $("#trackForm")[0].reset();
        }
    });
}