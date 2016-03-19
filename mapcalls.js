    var arr = [];
    function pollFromDatabase()
    {
      $.ajax({
        type: "GET",
        url: "poll_database.php",
        dataType: 'json',
        success: function (data) {
          for (var i = 0; i < data.length; i++) {
            arr[i] = $.map(data[i], function (el) {
              return el;
            });
          }
          flag = 1;
          console.log(data);
          console.log(arr);
        },
        error: function (xhr, status, error) {
          // check status && error
          alert(xhr + "\n" + status + "\n" + error);
        },
      });
    }

    function getMarkerFromLatLong(Lat, Long, Des)
    {
      var newMarker = new google.maps.Marker({map: map,position: new google.maps.LatLng(Lat, Long)});
      var infowindow = new google.maps.InfoWindow({content: Des});
      google.maps.event.addListener(newMarker, 'click', function(){infowindow.open(map,newMarker);});
      infowindow.open(map,newMarker);
      return newMarker;
    }

    function init_map()
    {
      var myOptions = {zoom:9,center:new google.maps.LatLng(49.2827291,-123.12073750000002),mapTypeId: google.maps.MapTypeId.ROADMAP};
      map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);
      //marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(49.2827291,-123.12073750000002)});
      marker = getMarkerFromLatLong(49.2827291, -123.12073750000002, "Hello");
      marker2 = getMarkerFromLatLong(50, -125, "Goodbye");
      marker3 = getMarkerFromLatLong(50, -125, "Goodbye");
      //marker2.setMap(map);

      pollFromDatabase();
    }

    google.maps.event.addDomListener(window, 'load', init_map);
/*
var myOptions = { zoom:10,
                  center:new google.maps.LatLng(49.2827291,-123.12073750000002),
                  mapTypeId: google.maps.MapTypeId.ROADMAP
                  };
map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);
marker = new google.maps.Marker({ map: map,
                                  position: new google.maps.LatLng(49.2827291,-123.12073750000002)
                                });
infowindow = new google.maps.InfoWindow({content:'<strong>City Goes Here</strong>'});

*/