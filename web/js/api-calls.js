var STORE_ORIGIN = window.location.origin;
var associate = {}
var keycloak = new Keycloak()

function initKeycloak(callback=null) {
    console.log('=========> initializing keycloak.');    
    return keycloak.init({
            onLoad: 'login-required'
        }).then(function(authenticated){
            if(authenticated) {
                console.log("=========> Authenticated as " + keycloak.idTokenParsed.name);
                loadAssociate(keycloak.subject, callback);
            } else {
                alert("Authentication failed");
            }

            return keycloak;
        });
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

function loadAssociate(id, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/associates/' + id;
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);
        },
        complete: function(response, status){
            var userAlreadyExists = false;
            if(status=='nocontent'){
                associate['userid'] = id;
                params = new URLSearchParams(window.location.search)
                if (!params.get('register'))
                    location.href = 'setup.html?register=true'
            } else {
                userAlreadyExists = true;
                associate = jQuery.parseJSON(response.responseText);
            }
            if(typeof callbackFunction == 'function') 
                callbackFunction();        
        }
    });
};

function saveAssociate(the_associate, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/associates';
    $.ajax({
        type: "POST",
        url: theUrl,
        data: JSON.stringify(the_associate),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function(response, status){
            var associate = jQuery.parseJSON(response.responseText);
            callbackFunction(associate);
        }
    });
};

function updateAssociate(the_associate, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/associates';
    $.ajax({
        type: "PUT",
        url: theUrl,
        data: JSON.stringify(the_associate),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function(response, status){
            callbackFunction();
        }
    });
};

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
    var theUrl = STORE_ORIGIN + '/api/trips/' + associate.rhid + "/" + year + "/" + month;
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
    var theUrl = STORE_ORIGIN + '/api/reports/' + associate.userid + "/" + year + '/' + month;
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
    var theUrl = STORE_ORIGIN + '/api/archive/' + associate.rhid + "/" + year;
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
    var theUrl = STORE_ORIGIN + '/api/archive/' + associate.rhid + "/" + filename;
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
