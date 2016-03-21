/*---------------------------------------------------------------------------------------
--  Source File:    mapcalls.js - Javascript file to dynamically create and append the
--                                google map canvas
--
--  Methods:      pollFromDatabase
--                updateMap
--                grabNewestEntry  
--                getMarkerFromLatLong   
--                init_map
--
--  Date:         March 19, 2016
--
--  Revisions:    March 20, 2016 (Tyler Trepanier) 
--                    Final integration, allowed for continuous updates
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
--  Notes: Javascript calls which will include the map inside of the webpage and then
--  update the map every second.
---------------------------------------------------------------------------------------*/
var arr = [];
var newestEntry;

/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: pollFromDatabase
--
-- DATE: March 19, 2016
--
-- REVISIONS: (Date and Description)
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
-- INTERFACE: function pollFromDatabase()
--
-- RETURNS: void
--
-- NOTES: Grabs the initial map data from the server and adds it to the map.
----------------------------------------------------------------------------------------------------------------------*/
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

/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: pollFromDatabase
--
-- DATE: March 20, 2016
--
-- REVISIONS: (Date and Description)
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
-- INTERFACE: function updateMap()
--
-- RETURNS: void
--
-- NOTES: Sets a time interval to allow the map to append each new recent update.
----------------------------------------------------------------------------------------------------------------------*/
function updateMap()
{
  setInterval(grabNewestEntry, 1000);
}

/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION:  grabNewestEntry
--
-- DATE:      March 20, 2016
--
-- REVISIONS: (Date and Description)
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
-- INTERFACE: function grabNewestEntry()
--
-- RETURNS: void
--
-- NOTES: Grabs the most recent map update and appends the map with only new entries.
----------------------------------------------------------------------------------------------------------------------*/
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

/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION:  getMarkerFromLatLong
--
-- DATE:      March 17, 2016
--
-- REVISIONS: (Date and Description)
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
-- INTERFACE: function getMarkerFromLatLong(Lat        Latitude of the location 
--                                          Long       Longitude of the location
--                                          Des        Description to be added into the info box
--                                          )
--
-- RETURNS: void
--
-- NOTES: Appends a map with a new marker from the Latitude, Longitude and the Description.
----------------------------------------------------------------------------------------------------------------------*/
function getMarkerFromLatLong(Lat, Long, Des)
{
  var newMarker = new google.maps.Marker({map: map,position: new google.maps.LatLng(Lat, Long)});
  var infowindow = new google.maps.InfoWindow({content: Des});
  google.maps.event.addListener(newMarker, 'click', function(){infowindow.open(map,newMarker);});
  return newMarker;
}

/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION:  init_map
--
-- DATE:      March 17, 2016
--
-- REVISIONS: (Date and Description)
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
-- INTERFACE: function init_map()
--
-- RETURNS: void
--
-- NOTES: Initializes the map at BCIT and zooms in to a reasonable amount.
----------------------------------------------------------------------------------------------------------------------*/
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