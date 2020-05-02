var distance = 0;
var locationName = '';
var numberOfTrips = 0;
var totalMileage = 0;

function setUserId() {
    if (localStorage.rhid) {
      $('#userName').text(localStorage.name);
    }
};
  
function initLocations() {
    $.ajax({
        url: '/rs/locations',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            formatLocationList(data.locations);
            //$("#the_response").text(data.result);
        },
        error: function(){
            console.log("===> error while loading locations, probably CORS denied the request.")
        }
    });
};

function formatLocationList(locations) {
    $('#tList').empty();
    var options = "";
    $.each(locations, function (index, location) {
        options += "<option value='" + location.destination + "'>" + location.distance + "</option>";
    });
    $('#tList').append(options);
};
  
function getTrips(month, year) {
    var theUrl = '/rs/trips/' + localStorage.rhid + "/" + year + "/" + month;
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            $.each(data.trips, function (index, trip) {
                addTripToTable(trip)
            });
            formatTripsTable(data.trips);
            //$("#the_response").text(data.result);
        }
    });
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
    console.log('====> ' + trip.rhid);
    console.log('====> ' + trip.date.day + '/' + trip.date.month + '/' + trip.date.year);
    console.log('====> ' + trip.location.destination + ', ' + trip.location.distance);
    console.log('====> ' + trip.purpose);
  var rowContent = '<tr>';
  rowContent += '<td>' + trip.date.day + '</td>';
  rowContent += '<td>' + trip.location.destination + '</td>';
  rowContent += '<td>' + trip.purpose + '</td>';
  rowContent += '<td>' + trip.location.distance + '</td>';
  rowContent += '</tr>';
  numberOfTrips += 1;
  totalMileage += parseInt(trip.location.distance, 10);
  document.getElementById("bannerNumberOfTrips").innerHTML = numberOfTrips;
  document.getElementById("bannerTotalMIleage").innerHTML = totalMileage;
  $('#tbl_round_list > tbody:last-child').append(rowContent);
};

function saveTrip() {
    var the_trip = {};
    the_trip.rhid = localStorage.rhid;
    var the_date = {};
    the_date.day = $('#inputDay').val();
    the_date.month = $('#inputMonth :selected').val();
    the_date.year = $('#inputYear :selected').val();
    the_trip.date = the_date;
    var the_location = {};
    the_location.destination = locationName;
    the_location.distance = $('#inputDistance').val();
    the_trip.location = the_location;
    the_trip.purpose = $('#inputPurpose').val();
    console.log(data);
    $.ajax({
          type: "POST",
          url: "/rs/trips",
          data: the_trip,
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
            addTripToTable(the_trip);
          }
    });
};