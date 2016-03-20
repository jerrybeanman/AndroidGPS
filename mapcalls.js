    var arr = [];
    var newestEntry;
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
          console.log("<<Initial poll>>");
          console.log(data);
          console.log(arr);
          newestEntry = arr[arr.length - 1];

          for (var i = 0; i < arr.length; i++)
          {
            getMarkerFromLatLong(arr[i][2], 
                                 arr[i][3], 
                                 arr[i][0] + " located here at <br/>" + arr[i][1]);
          }
        },
        error: function (xhr, status, error) {
          // check status && error
          alert(xhr + "\n" + status + "\n" + error);
        },
      });
    }

    function updateMap()
    {
      setTimeout(grabNewestEntry, 2000);
    }

    function grabNewestEntry()
    {
      $.ajax({
        type: "GET",
        url: "grab_newest_user.php",
        dataType: 'json',
        success: function (data) {
          for (var i = 0; i < data.length; i++) {
            temp[i] = $.map(data[i], function (el) {
              return el;
            });
          }
          flag = 1;
          console.log("<<Newest>>");
          console.log(data);
          console.log(temp);

          if(newestEntry[1] !== temp[0][1])
          {
            console.log("Updated location");
            /*
            newestEntry = temp[0];
            var newMarker = getMarkerFromLatLong(newestEntry[2], newestEntry[3], newestEntry[0]);
            */
          }
          else
          {
            console.log("No recent update.");
          }
          
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