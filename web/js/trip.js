var the_destination = {};

function initMap() {
    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer();

    const componentForm = [
        'location',
    ];
 
    const rome_office_position = {lat: 41.909832599799316, lng: 12.452532095955236};

    const options = {zoom: 13, zoomControl: true, mapTypeControl: false, streetViewControl: false, scaleControl: true, center: rome_office_position};
    map = new google.maps.Map(document.getElementById('map'), options);

    const image = "/images/rh_flag.png";
    const rome_office_marker = new google.maps.Marker({
        map: map,
        title: "Red Hat sede di Roma",
        position: rome_office_position,
        icon: image,
        draggable: false
    });
    const marker = new google.maps.Marker({map: map, draggable: false});
    marker.setVisible(false);

    directionsRenderer.setMap(map);  
    const onChangeHandler = function () {
        directionsService.route(
            {
                origin: rome_office_position,
                destination: the_destination,
                travelMode: google.maps.TravelMode.DRIVING
            },
            (response, status) => {
                calculateRouteDistance(response.routes[0].legs[0].distance.value);
                console.log(status);
                directionsRenderer.setDirections(response);
            }
        )
    };
    document.getElementById("checkOutButton").addEventListener("click", onChangeHandler);

    const autocompleteInput = document.getElementById('location');
    const autocomplete = new google.maps.places.Autocomplete(autocompleteInput, {
      fields: ["address_components", "geometry", "name"],
      types: ["address"],
    });
    autocomplete.addListener('place_changed', function () {
        const place = autocomplete.getPlace();
        if (!place.geometry) {
            // User entered the name of a Place that was not suggested and
            // pressed the Enter key, or the Place Details request failed.
            window.alert('No details available for input: \'' + place.name + '\'');
            return;
        }
        $('#distance').html('');
        renderAddress(place);
        fillInAddress(place);
    });

    function fillInAddress(place) {  // optional parameter
        const addressNameFormat = {
            'street_number': 'short_name',
            'route': 'long_name',
            'locality': 'long_name',
            'administrative_area_level_1': 'short_name',
            'country': 'long_name',
            'postal_code': 'short_name',
        };
        const getAddressComp = function (type) {
            for (const component of place.address_components) {
                if (component.types[0] === type) {
                    return component[addressNameFormat[type]];
                }
            }
            return '';
        };
        document.getElementById('location').value = getAddressComp('street_number') + ' ' + getAddressComp('route');
    };

    function renderAddress(place) {
      map.setCenter(place.geometry.location);
      the_destination = place.geometry.location;
      marker.setPosition(place.geometry.location);
      marker.setVisible(true);
    };
};

function calculateAndDisplayRoute(directionsService, directionsRenderer) {
    directionsService.route(
        {
            origin: {lat: 41.909832599799316, lng: 12.452532095955236},
            destination: {lat: 41.831200940073444, lng: 12.467770885340915},
            travelMode: google.maps.TravelMode.DRIVING
        },
        (response, status) => {
            console.log(response);
            console.log(status);
            directionsRenderer.setDirections(response);
        }
    )
};

function calculateRouteDistance(routeDistance) {
    const totalDistance = Math.ceil((routeDistance * 2.4) / 1000);
    $('#distance').html(totalDistance);
};