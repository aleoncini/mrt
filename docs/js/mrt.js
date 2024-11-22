var STORE_ORIGIN = window.location.origin;

const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const options = { weekday: 'long', year: 'numeric', month: 'short', day: 'numeric' };

function configReportForm() {
    if(report.latestOdometer == null || report.latestOdometer == 0){
        report.latestOdometer = 0;
    }
    $('#odoStartInput').val(report.latestOdometer + calculateOdometerStart());
};

function calculateOdometerStart(dayOfTrip) {
    return 0;
};

function setAlert(msg) {
    $("#formNotComplete").html(msg);
    $("#formNotComplete").fadeIn();
    window.setTimeout(function() {
        $("#formNotComplete").fadeOut();
    }, 3000);
};

function resetReport() {
    var trips = [];
    localStorage.setItem('mrtTrips', JSON.stringify(trips));
    displayAllTrips();
};

function displayAllTrips() {
    $('#current_report').hide();
    $("#tripsTableBody").empty();

    // check if an array of trips is locally saved
    var trips = localStorage.getItem('mrtTrips');
    var trips = JSON.parse(localStorage.getItem("mrtTrips") || "[]");

    $.each(trips, function (index, trip) {
        addTripToTable(trip)
    });

    $('#current_report').show(500);
};

function addTripToTable(trip) {
    var rowContent = '<tr>';
    rowContent += '<td>' + trip.date + '</td>';
    rowContent += '<td>' + trip.odometerStart + '</td>';
    rowContent += '<td>' + trip.from + '</td>';
    rowContent += '<td>' + trip.destination + '</td>';
    rowContent += '<td><img src="img/';
    if(trip.twoWays){
        rowContent += 'check-';
    }
    rowContent += 'circle.svg" width="24" height="24"></td>';
    rowContent += '<td>' + trip.purpose + '</td>';
    rowContent += '<td>' + trip.odometerEnd + '</td>';
    rowContent += '<td>' + trip.distance + '</td>';
    rowContent += '<td style="cursor: pointer;" class="delete_trip"><img src="img/trash.svg" alt="delete" width="24" height="24"></td>';
    rowContent += '</tr>';
    $('#tbl_trips  tbody').append(rowContent);
};

function setOdometerStart() {
    console.log("=======================");
};
