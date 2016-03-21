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

          newestEntry = arr[arr.length - 1];

          for (var i = 0; i < arr.length; i++)
          {
            var newMarker = getMarkerFromLatLong(arr[i][2], arr[i][3], 
              "<strong>" + arr[i][0] + "</strong> using the device: " + arr[i][4] 
              + " <br />was here at <i>" + arr[i][1] 
              + "</i><br />with the ip address of: " + arr[i][5] 
              + "<br />Lat: " + arr[i][2] + " Long: " + arr[i][3]);
          }
        },
        error: function (xhr, status, error) {
          // check status && error
          alert(xhr + "\n" + status + "\n" + error);
        },
      });
    }

    // Update the map every second.
    function updateMap()
    {
      setInterval(grabNewestEntry, 1000);
    }

    // Grab only one of the most recent updates
    function grabNewestEntry()
    {
      var temp = [];
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

          if(newestEntry[1] !== temp[0][1]) // If we have a recent update
          {
            console.log("Updated location");
            
            newestEntry = temp[0];
            var newMarker = getMarkerFromLatLong(temp[0][2], temp[0][3], "<strong>" + temp[0][0] + "</strong> using the device: " + temp[0][4] + " <br />was here at <i>" + temp[0][1] + "</i><br />with the ip address of: " + temp[0][5] + "<br />Lat: " + temp[0][2] + " Long: " + temp[0][3]);
             
          }
          else
          {
            console.log("No recent update.");
          }
          
        },
        error: function (xhr, status, error) {
          // check status && error
          //alert(xhr + "\n" + status + "\n" + error);
        },
      });
    }

    // Create a new marker using it's latitude, longitude and a description to put in the info box.
    function getMarkerFromLatLong(Lat, Long, Des)
    {
      var newMarker = new google.maps.Marker({map: map,position: new google.maps.LatLng(Lat, Long)});
      var infowindow = new google.maps.InfoWindow({content: Des});
      google.maps.event.addListener(newMarker, 'click', function(){infowindow.open(map,newMarker);});
      infowindow.open(map,newMarker);
      return newMarker;
    }

    // Initialize the map and center it to BCIT.
    function init_map()
    {
      var myOptions = {zoom:9,center:new google.maps.LatLng(49.249838,-123.001424),mapTypeId: google.maps.MapTypeId.ROADMAP};
      map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);
      marker = getMarkerFromLatLong(49.249838, -123.001424, "British Columbia Institute of Technology");

      pollFromDatabase();
      updateMap();
    }

    //Initialize the map on the website.
    google.maps.event.addDomListener(window, 'load', init_map);