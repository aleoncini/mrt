function nextStep() {
    $('#first_step_settings').hide();
    $('#settings_form').show(500);
};

function previousStep() {
    $('#settings_form').hide();
    $('#first_step_settings').show(500);
};
  
function loadUserForSetup(rhid) {
    var theUrl = '/rs/associates/' + rhid;
    $.get(theUrl, function(data) {
        var data = jQuery.parseJSON(response.responseText);
        if (data.id === "none"){
            $("#unregistered_user_alert").fadeIn();
        } else {
            localStorage.setItem("playerid", data.id);
            localStorage.setItem("playername", data.name);
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
  
  