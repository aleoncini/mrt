var STORE_ORIGIN = window.location.origin;
var banner = new Image();

const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const options = { weekday: 'long', year: 'numeric', month: 'short', day: 'numeric' };

function setAlert(msg) {
    $("#formNotComplete").html(msg);
    $("#formNotComplete").fadeIn();
    window.setTimeout(function() {
        $("#formNotComplete").fadeOut();
    }, 3000);
};

function closeAssociateInfoAlert() {
    $("#associateInfoFormNotCorrect").fadeOut();
};

function checkConfig() {
    // check if associate name and other info are already saved on this browser
    // in case they are not, will ask with a modal form.
    var associateName = localStorage.getItem('associateName');
    if(associateName == null){
        $("#enterAssociateInfoModal").modal('show');
    }
};

function resetReport() {
    var trips = [];
    localStorage.setItem('mrtTrips', JSON.stringify(trips));
    displayAllTrips();
};

function setOdometerStart() {
    if ($('#dateInput').val().length == 0) {
        console.log("==========> dateInput size = 0");
        return;
    }
    var t_date = Date.parse($('#dateInput').val());
    var trips = JSON.parse(localStorage.getItem("mrtTrips") || "[]");
    if(trips.length == 0){
        console.log("==========> trips array size = 0");
        return;
    }
    var lastTrip = trips[trips.length - 1];
    var lastDate = new Date(lastTrip.date);
    const diffTime = Math.abs(t_date - lastDate);
    const diffDays = (Math.floor(diffTime / (1000 * 60 * 60 * 24))) -1;
    var latestOdometer = Number(localStorage.getItem("latestOdometer") || 0);
    var odometerStart = 0;
    var randomDistance = diffDays * Math.floor(Math.random() * 30);
    odometerStart = latestOdometer + randomDistance;
    $('#odoStartInput').val(odometerStart);
};

function displayAllTrips() {
    $('#current_report').hide();
    $("#tripsTableBody").empty();

    // check if an array of trips is locally saved
    var trips = JSON.parse(localStorage.getItem("mrtTrips") || "[]");

    $.each(trips, function (index, trip) {
        addTripToTable(trip, index)
    });

    $('#current_report').show(500);
};

function addTripToTable(trip, ndx) {
    var tripDate = new Date(trip.date).toLocaleDateString('it-IT', options);
    var rowContent = '<tr>';
    rowContent += '<td>' + tripDate + '</td>';
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
    //console.log("=====> " + trip.date + " ===== " + ndx);
    rowContent += '<td style="cursor: pointer;" class="delete_trip" data-id="' + ndx + '"><img src="img/trash.svg" alt="delete" width="24" height="24"></td>';
    rowContent += '</tr>';
    $('#tbl_trips  tbody').append(rowContent);
};

function loadBanner(url) {
    banner.onError = function() {
        alert('Cannot load image: "'+url+'"');
    };
    banner.onload = function() {
    };
    banner.src = url;
}

function saveAsPdf() {
    var name = localStorage.getItem('associateName');
    var rate = localStorage.getItem("mileageRate") || -1;
    var type = localStorage.getItem('vehicleType') || "Unknown";
    var now = new Date();
    var filename = 'mileage-report-' + now.getDate() + '-' + months[now.getMonth()] + '-' + now.getFullYear() + '.pdf';
    var doc = new jsPDF();
    var width = doc.internal.pageSize.width;
    var height = doc.internal.pageSize.height;
    var X = 0;
    var Y = 0;
    var textWidth = 0;
    var text = '';
    doc.addImage(banner, "PNG", 5, 5, 200, 23);

    doc.setFont('helvetica', 'bold');
    doc.setTextColor('#ffffff');
    doc.setFontSize(10);
    doc.text('Business Mileage Reimbursement Form', 10, 16);

    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#ffffff');
    doc.setFontSize(8);
    doc.text('prepared using MRTool', 10, 20);

    text = 'Report Date: ' + now.toLocaleDateString('it-IT', { weekday:"long", day:"numeric", month:"short", year:"numeric"});
    textWidth = doc.getTextWidth(text);
    X = width - 5 - textWidth;
    doc.setTextColor('#004099');
    doc.text(text, X, 32);

    doc.setDrawColor('#004099');
    doc.setLineWidth(0.5);
    doc.line(5, 45, 205, 45);

    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#3070aa');
    doc.setFontSize(10);
    doc.text('Associate Name:', 12, 40);
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(14);
    doc.text(name, 60, 40);

    doc.setFillColor('#ff0000');
    doc.rect(5, 50, 200, 7, 'F');

    doc.setDrawColor('#ffffff');
    doc.setLineWidth(0.5);
    doc.line(30, 50, 30, 57);
    doc.line(55, 50, 55, 57);
    doc.line(85, 50, 85, 57);
    doc.line(115, 50, 115, 57);
    doc.line(125, 50, 125, 57);
    doc.line(165, 50, 165, 57);
    doc.line(190, 50, 190, 57);

    doc.setFont('helvetica', 'bold');
    doc.setTextColor('#ffffff');
    doc.setFontSize(8);
    text = 'date';
    textWidth = doc.getTextWidth(text);
    X = 17 - (textWidth/2);
    doc.text(text, X, 54);
    text = 'start (km)';
    textWidth = doc.getTextWidth(text);
    X = 42 - (textWidth/2);
    doc.text(text, X, 54);
    text = 'from';
    textWidth = doc.getTextWidth(text);
    X = 70 - (textWidth/2);
    doc.text(text, X, 54);
    text = 'to';
    textWidth = doc.getTextWidth(text);
    X = 100 - (textWidth/2);
    doc.text(text, X, 54);
    text = 'purpose';
    textWidth = doc.getTextWidth(text);
    X = 145 - (textWidth/2);
    doc.text(text, X, 54);
    text = 'end (km)';
    textWidth = doc.getTextWidth(text);
    X = 177 - (textWidth/2);
    doc.text(text, X, 54);
    text = 'mileage';
    textWidth = doc.getTextWidth(text);
    X = 197 - (textWidth/2);
    doc.text(text, X, 54);

    var totalMileage = 0;
    var trips = JSON.parse(localStorage.getItem("mrtTrips") || "[]");

    $.each(trips, function (index, trip) {
        var Y = 62 + (6 * index);
        if(isOdd(index)){
            doc.setFillColor('#eeeeee');
            doc.rect(5, (Y-4), 200, 6, 'F');
            doc.setDrawColor('#ffffff');
            doc.setLineWidth(0.5);
            doc.line(30, (Y-4), 30, (Y+2));
            doc.line(55, (Y-4), 55, (Y+2));
            doc.line(85, (Y-4), 85, (Y+2));
            doc.line(115, (Y-4), 115, (Y+2));
            doc.line(125, (Y-4), 125, (Y+2));
            doc.line(165, (Y-4), 165, (Y+2));
            doc.line(190, (Y-4), 190, (Y+2));
        }
        doc.setFont('helvetica', 'normal');
        doc.setTextColor('#000000');
        doc.setFontSize(8);
        var original = trip.date.toLocaleDateString('it-IT', options);
        text = original.substr(original.indexOf(" ") + 1);
        textWidth = doc.getTextWidth(text);
        X = 17 - (textWidth/2);
        doc.text(text, X, Y);
        text = trip.odometerStart.toString();
        textWidth = doc.getTextWidth(text);
        X = 42 - (textWidth/2);
        doc.text(text, X, Y);
        text = trip.from;
        textWidth = doc.getTextWidth(text);
        X = 70 - (textWidth/2);
        doc.text(text, X, Y);
        text = trip.destination;
        textWidth = doc.getTextWidth(text);
        X = 100 - (textWidth/2);
        doc.text(text, X, Y);
        text = '';
        if(trip.twoWays){
            text = '2way';
            textWidth = doc.getTextWidth(text);
            X = 120 - (textWidth/2);
            doc.text(text, X, Y);
        }
        text = trip.purpose;
        textWidth = doc.getTextWidth(text);
        X = 145 - (textWidth/2);
        doc.text(text, X, Y);
        text = trip.odometerEnd.toString();
        textWidth = doc.getTextWidth(text);
        X = 177 - (textWidth/2);
        doc.text(text, X, Y);
        text = trip.distance.toString();
        textWidth = doc.getTextWidth(text);
        X = 200 - (textWidth);
        doc.text(text, X, Y);
        totalMileage += trip.distance;
    });

    doc.setDrawColor('#004099');
    doc.setLineWidth(0.5);
    doc.line(5, 210, 205, 210);

    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#3070aa');
    doc.setFontSize(8);
    doc.text('Mileage rate:', 12, 218);
    doc.text('Vehicle Type:', 12, 222);

    doc.setFont('helvetica', 'bold');
    doc.setTextColor('#004099');
    doc.setFontSize(10);
    doc.text(rate.toString(), 40, 218);

    doc.setFont('helvetica', 'normal');
    doc.text(type, 40, 222);

    doc.setDrawColor('#eeeeee');
    doc.line(120, 222, 205, 222);
    doc.setFillColor('#eeeeee');
    doc.rect(175, 222, 30, 12, 'F');

    doc.setFontSize(8);
    text = 'Total mileage for month (km)';
    textWidth = doc.getTextWidth(text);
    X = width - 45 - textWidth;
    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#000000');
    doc.text(text, X, 220);

    doc.setFontSize(8);
    text = 'Total COST for month (Euro)';
    textWidth = doc.getTextWidth(text);
    X = width - 45 - textWidth;
    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#000000');
    doc.text(text, X, 230);

    doc.setFontSize(10);
    text = totalMileage.toString();
    textWidth = doc.getTextWidth(text);
    X = width - 10 - textWidth;
    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#000000');
    doc.text(text, X, 220);

    doc.setFontSize(10);
    text = '' + (totalMileage * rate).toFixed(2);
    textWidth = doc.getTextWidth(text);
    X = width - 10 - textWidth;
    doc.setFont('helvetica', 'bold');
    doc.setTextColor('#000000');
    doc.text(text, X, 230);

    doc.setLineWidth(0.8);
    doc.setDrawColor('#000000');
    doc.rect(5, 245, 200, 47);

    doc.setFont('helvetica', 'bold');
    doc.setTextColor('#101010');
    doc.setFontSize(8);
    doc.text('Internal/External Audit Requirement:', 12, 250);

    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#202020');
    doc.text('1. Associates must maintain Supporting Vehicle Documentation (Insurance Certificate or Registration Card) at all times.', 12, 255);
    doc.text('2. Red Hat may request Supporting Vehicle Documentation (Insurance Certificate or Registration Card) from Associates to', 12, 260);
    doc.text('    ensure compliance with Italian regulatory requirements.', 12, 265);
    doc.text('3. If requested, Associates must provide Supporting Vehicle Documentation (Insurance Certificate or Registration Card)', 12, 270);
    doc.text('    within 24-48 hours of such request.', 12, 275);
    doc.text('I acknowledge here that I am aware of the terms listed above.', 12, 282);

    doc.save(filename);
};

function isOdd(n) {
    return Math.abs(n % 2) == 1;
}

