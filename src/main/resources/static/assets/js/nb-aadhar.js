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
    activateLinks: false,
    afterRouteChange: function(){
        $('#filearray').on('change', function(e){
            var fileName = '';
		    if(this.files)
                fileName = e.target.value.split( '\\' ).pop();
            var idxDot = fileName.lastIndexOf(".") + 1;
            var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
            if(fileName.trim()){
                if (extFile=="jpg" || extFile=="jpeg"){
                    $('#fileArrayLabel')[0].innerHTML = fileName;
                    $('#fileArrayLabel').addClass('files-added');
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

var submitNewRegistry = function(){
    var fd = new FormData($("#newRegistryForm")[0]);

    $.ajax({
        url: '/BabyIdentification/api/upload/files',
        data: fd,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data){
            //alert(data);
            $('#nb_aadharId')[0].innerHTML = data.id;
        }
    });
}

var submitMissingCase = function(){
    var fd = new FormData($("#missingReportForm")[0]);

    $.ajax({
        url: '/BabyIdentification/api/upload/missing',
        data: fd,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data){
            alert(data);
            //$('#nb_aadharId')[0].innerHTML = data.id;
        }
    });
}