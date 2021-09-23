//var STORE_ORIGIN = 'http://store-ossg.apps.ocp4.rhocplab.com/';
var STORE_ORIGIN = 'http://localhost:8080/';

function formatLocationList(locations) {
    $('#tList').empty();
    var options = "";
    $.each(locations, function (index, location) {
        options += "<option value='" + location.destination + "'>" + location.distance + "</option>";
    });
    $('#tList').append(options);
};

function displayAllTrips(trips) {
    $.each(trips, function (index, trip) {
        addTripToTable(trip)
    });
    $('#tripsBannerContainer').show();
    $('#tripsTableContainer').show(500);
};

function refreshTripsTable() {
    $('#tripsTableContainer').hide();
    $('#tripsBannerContainer').hide();
    $("#tripsTableBody").empty();

    numberOfTrips = 0;
    totalMileage = 0;
    document.getElementById("bannerNumberOfTrips").innerHTML = '0';
    document.getElementById("bannerTotalMileage").innerHTML = '0';

    $('#tripsBannerContainer').show();
    $('#tripsTableContainer').show(500);
};

function addTripToTable(trip) {
    var numberOfTrips = parseInt($('#bannerNumberOfTrips').text());
    var totalMileage = parseInt($('#bannerTotalMileage').text(), 10);
    var rowContent = '<tr>';
    rowContent += '<td>' + trip.day + '</td>';
    rowContent += '<td>' + trip.destination + '</td>';
    rowContent += '<td>' + trip.purpose + '</td>';
    rowContent += '<td>' + trip.distance + '</td>';
    rowContent += '</tr>';
    $('#tbl_trips  tr:last').after(rowContent);
    numberOfTrips += 1;
    totalMileage += trip.distance;
    $('#bannerNumberOfTrips').text(numberOfTrips);
    $('#bannerTotalMileage').text(totalMileage);
};

// --- STORAGE FUNCTIONS ----------------------------------------- 
function saveTrip(callbackFunction) {
    var theUrl = STORE_ORIGIN + 'trips';
    var the_trip = {};
    the_trip.day = parseInt($('#inputDay').val());
    the_trip.month = parseInt($('#inputMonth :selected').val());
    the_trip.year = parseInt($('#inputYear :selected').val());
    the_trip.destination = $('#inputLocationName').val();
    the_trip.distance = parseInt( $('#inputDistance').val(), 10);
    the_trip.purpose = $('#inputPurpose').val();
    $.ajax({
        type: "POST",
        url: theUrl,
        data: JSON.stringify(the_trip),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function(response, status, xhr){
            console.log(response.responseText);
            var trip = jQuery.parseJSON(response.responseText);
            callbackFunction(trip);
        }
    });
};

function loadTrips(year, month, callbackFunction) {
    var theUrl = STORE_ORIGIN + 'trips/' + year + "/" + month;
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        complete: function(response, status, xhr){
            var trips = jQuery.parseJSON(response.responseText);
            callbackFunction(trips);
        }
    });
};