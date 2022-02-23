var STORE_ORIGIN = window.location.origin;

var userId = null;
var keycloak = new Keycloak();

function initKeycloak(callback=null) {
    console.log('=========> initializing keycloak!!');
    keycloak.init({
        onLoad: 'login-required'
    }).then(function(authenticated){
        if(authenticated) {
            userId = keycloak.subject;
            console.log("Authenticated as " + keycloak.idTokenParsed.preferred_username)
            if (typeof callback == 'function')
                callback()
        }
        else
            console.log("Authentication failed")
    })
}


// --- GOOGLE MAPS FUNCTIONS ----------------------------------------- 

function initKey() {
    var theUrl = STORE_ORIGIN + '/api/key';
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            var keyObject = jQuery.parseJSON(response.responseText);
            console.log('Key READY...');
            initGoogleScript(keyObject.value);
        }
    });
};

function initGoogleScript(theKey) {
    var the_script = document.createElement( 'script' );
    var the_src = 'https://maps.googleapis.com/maps/api/js?key=' + theKey + '&callback=initMap&libraries=places';
    the_script.setAttribute( 'src', the_src );
    the_script.setAttribute('defer', null);
    the_script.setAttribute('async', null);
    console.log('Appending the script...');
    document.body.appendChild( the_script );
};

// --- STORAGE FUNCTIONS ----------------------------------------- 

function saveTrip(the_trip, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/trips';
    $.ajax({
        type: "POST",
        url: theUrl,
        data: JSON.stringify(the_trip),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            var trip = jQuery.parseJSON(response.responseText);
            callbackFunction(trip);
        }
    });
};

function deleteTrip(id) {
    var theUrl = STORE_ORIGIN + '/api/trips/' + id;
    $.ajax({
        url: theUrl,
        type: 'DELETE',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            console.log('TRIP ' + id + ' removed from DB!');
        }
    });
};

function loadTrips(year, month, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/trips/' + userId + "/" + year + "/" + month;
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            var trips = jQuery.parseJSON(response.responseText);
            callbackFunction(trips);
        }
    });
};

function requestReportBuild(month, callbackFunction) {
    var year = new Date().getFullYear();
    var theUrl = STORE_ORIGIN + '/api/reports/' + userId + "/" + year + '/' + month;
    $.ajax({
        url: theUrl,
        type: 'POST',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            var answer = jQuery.parseJSON(response.responseText);
            callbackFunction(answer);
        }
    });
};

// --- PDF ARCHIVE FUNCTIONS ----------------------------------------- 

function loadReportList(year, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/archive/' + userId + "/" + year;
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            callbackFunction(data);
        }
    });
};

function deleteReportFile(filename) {
    var theUrl = STORE_ORIGIN + '/api/archive/' + userId + "/" + filename;
    $.ajax({
        url: theUrl,
        type: 'DELETE',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            console.log('FILE ' + filename + ' removed from file system!');
        }
    });
};

function getReport(filename){
    var theUrl = STORE_ORIGIN + '/api/archive/' + userId + "/" + filename;
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
          },
        complete: function(response, status, xhr){
            window.open(response.responseText);
        }
    });
}
