var STORE_ORIGIN = window.location.origin;


function formatLocationList(locations) {
    $('#tList').empty();
    var options = "";
    $.each(locations, function (index, location) {
        options += "<option value='" + location.destination + "'>" + location.distance + "</option>";
    });
    $('#tList').append(options);
};

function displayAllTrips(trips) {
    $('#tripsTableContainer').hide();
    $("#tripsTableBody").empty();
    $('#bannerNumberOfTrips').text('0');
    $('#bannerTotalMileage').text('0');
    $.each(trips, function (index, trip) {
        addTripToTable(trip)
    });
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
    rowContent += '<td style="cursor: pointer;" class="delete_trip" data-id="' + trip.id + '"><img src="images/trash.svg" alt="delete" width="24" height="24"></td>';
    rowContent += '</tr>';
    $('#tbl_trips  tbody').append(rowContent);
    numberOfTrips += 1;
    totalMileage += trip.distance;
    $('#bannerNumberOfTrips').text(numberOfTrips);
    $('#bannerTotalMileage').text(totalMileage);
};

function showReportBuildResult(answer) {
    $("#reportSuccessfullyRequested").fadeIn();
    window.setTimeout(function () { 
        $("#reportSuccessfullyRequested").fadeOut();
    }, 2000);
};

function displayReportList(reportList) {
    $("#reportsTableBody").empty();
    $.each(reportList, function (index, reportInfo) {
        addReportInfoToTable(reportInfo);
    });
};

function addReportInfoToTable(info) {
    var rowContent = '<tr>';
    rowContent += '<td>' + info.month + '</td>';
    rowContent += '<td>' + info.creationTime + '</td>';
    rowContent += '<td>' + info.size + '</td>';
    rowContent += '<td>' + info.version + '</td>';
    rowContent += '<td><a href="/archive/pdf/' + info.name + '"><img src="images/file-earmark-arrow-down.svg" alt="download" width="32" height="32"></a></td>';
    rowContent += '<td style="cursor: pointer;" class="delete_report" data-filename="' + info.name + '"><img src="images/trash.svg" alt="delete" width="32" height="32"></td>';
    rowContent += '</tr>';
    $('#tbl_reports  tbody').append(rowContent);
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
        complete: function(response, status, xhr){
            console.log('TRIP ' + id + ' removed from DB!');
        }
    });
};

function loadTrips(year, month, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/trips/' + year + "/" + month;
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

function requestReportBuild(month, callbackFunction) {
    var year = new Date().getFullYear();
    var theUrl = STORE_ORIGIN + '/api/reports/' + year + '/' + month;
    $.ajax({
        url: theUrl,
        type: 'POST',
        dataType: 'json',
        complete: function(response, status, xhr){
            var answer = jQuery.parseJSON(response.responseText);
            callbackFunction(answer);
        }
    });
};

// --- PDF ARCHIVE FUNCTIONS ----------------------------------------- 

function loadReportList(year, callbackFunction) {
    var theUrl = STORE_ORIGIN + '/api/archive/' + year;
    $.ajax({
        url: theUrl,
        type: 'GET',
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            callbackFunction(data);
        }
    });
};

function deleteReportFile(filename) {
    var theUrl = STORE_ORIGIN + '/api/archive/' + filename;
    $.ajax({
        url: theUrl,
        type: 'DELETE',
        dataType: 'json',
        complete: function(response, status, xhr){
            console.log('FILE ' + filename + ' removed from file system!');
        }
    });
};
