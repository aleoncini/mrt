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

