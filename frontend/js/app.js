var locationName = '';
var distance = 0;
var numberOfTrips = 0;
var totalMileage = 0;

function checkUser() {
  if (localStorage.rhid) {
    $('#userName').text(localStorage.name);
  } else {
    $('#enterAssociateInfoModal').modal('show');
  }
};

function initLocations() {
  $.ajax({
      url: '/rs/locations',
      type: 'GET',
      data: {},
      dataType: 'json',
      complete: function(response, status, xhr){
          var data = jQuery.parseJSON(response.responseText);
          formatLocationList(data.locations);
          //$("#the_response").text(data.result);
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

function saveTrip() {
  var data = '{ "associateId": "' + localStorage.rhid + '", ';
  data += '"purpose": "' + $('#inputPurpose').val() + '", ';
  data += '"location": { "destination": "' + locationName + '", "distance": ' + $('#inputDistance').val() + '}, ';
  data += '"date": { "day": ' + $('#inputDay').val() + ', "month": ' + $('#inputMonth :selected').val() + ', "year": ' + $('#inputYear :selected').val() + ' } ';
  data += '}';
  console.log(data);
  $.ajax({
        type: "POST",
        url: "/rs/trips/add",
        data: data,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
          addTripToTable();
        }
  });
};

function saveUser() {
  // { "rhid": "9053",
  //   "name": "Andrea Leoncini",
  //   "email": "aleoncin@redhat.com",
  //   "costCenter": "420",
  //   "car": { "registryNumber":"FB214ZM", "mileageRate": 0.89 }
  // }
  if($('#inputYear').val() != "")
  var data = '{ "rhid": "' + $('#inputAssociateId').val() + '", ';
  data += '"name": "' + $('#inputAssociateName').val() + '", ';
  data += '"email": "' + $('#inputAssociateMail').val() + '", ';
  data += '"costCenter": "' + $('#inputAssociateCostCenter').val() + '", ';
  data += '"car": { "registryNumber": "' + $('#inputCarRegNumber').val() + '", "mileageRate": ' + $('#inputCarRate').val() + ' } ';
  data += '}';
  $.ajax({
        type: "POST",
        url: "/rs/associates/add",
        data: data,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
          localStorage.rhid = $('#inputAssociateId').val();
          localStorage.name = $('#inputAssociateName').val();
          alert("The tool is now setup to work with RHID: " + localStorage.rhid + ". If you want to act as different user go to setup page and save new info.");
          window.location="index.html";
        }
  });
};

function initTripsTable() {
    var month = $('#inputMonth').val();
    var year = $('#inputYear').val();
    var theUrl = '/rs/trips/' + localStorage.rhid + "/" + year + "/" + month;
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            formatTripsTable(data.trips);
            //$("#the_response").text(data.result);
        }
    });
};

function formatTripsTable(trips) {
    var tableContent = '<thead>';
    tableContent += '<tr><th>Day</th><th>From office to</th><th>purpose</th><th>mileage (Km)</th></tr>';
    tableContent += '</thead>';
    tableContent += '<tbody>';
    $.each(trips, function (index, trip) {
        tableContent += '<tr>';
        tableContent += '<td>' + trip.date.day + '</td>';
        tableContent += '<td>' + trip.location.destination + '</td>';
        tableContent += '<td>' + trip.purpose + '</td>';
        tableContent += '<td>' + trip.location.distance + '</td>';
        tableContent += '</tr>';
        numberOfTrips += 1;
        totalMileage += trip.location.distance;
    });
    tableContent += '</tbody>';

    document.getElementById("bannerMonth").innerHTML = $("#inputMonth option:selected").text();
    document.getElementById("bannerYear").innerHTML = $("#inputYear option:selected").text();
    document.getElementById("bannerNumberOfTrips").innerHTML = numberOfTrips;
    document.getElementById("bannerTotalMIleage").innerHTML = totalMileage;
    document.getElementById("tbl_round_list").innerHTML = tableContent;
};

function addTripToTable() {
  var rowContent = '<tr>';
  rowContent += '<td>' + $('#inputDay :selected').val() + '</td>';
  rowContent += '<td>' + locationName + '</td>';
  rowContent += '<td>' + $('#inputPurpose').val() + '</td>';
  rowContent += '<td>' + distance + '</td>';
  rowContent += '</tr>';
  numberOfTrips += 1;
  totalMileage += parseInt(distance, 10);
  document.getElementById("bannerNumberOfTrips").innerHTML = numberOfTrips;
  document.getElementById("bannerTotalMIleage").innerHTML = totalMileage;
  $('#tbl_round_list > tbody:last-child').append(rowContent);
};
