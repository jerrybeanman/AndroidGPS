

<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

  <style>
    @font-face {
      font-family: myFirstFont;
      src: url(Bitwise.ttf);
    }
    /*
    blue-green: #072324; 
    forest-green: #092F0B;
    green-yellow: #28380A;
    soft-blue: 1F4446;
    DARK-green: 000B01;
    */
    body {
      background-color: black;
      color: green;
    }

    h1 {
      font-family: myFirstFont;
    }

    .jumbotron {
      background-color: #072324;
      color: green;
    }

    .jumbotron > p {
      margin-bottom: 0;
      padding-bottom: 0;
    }

    .foot {
      margin-bottom: 0px;
      margin-top: 30px;
      padding-bottom: 10;
      text-align: center;
    }
  </style>

</head>
<body>
  <div class="container">
    <div class="jumbotron">
      <h1>You have been hacked!</h1>
      <p>Approved by the NSA...</p>
    </div>
  </div>
  <script src='https://maps.googleapis.com/maps/api/js?v=3.exp'></script>
  <div id="canvas" style='overflow:hidden;height:440px;width:700px;'>
    <div id='gmap_canvas' style='height:440px;width:700px;'></div>
    <style>#gmap_canvas img{max-width:none!important;background:none!important} #canvas {margin: auto;} </style>
  </div>
  <script type='text/javascript'>
    function getMarkerFromLatLong(Lat, Long, Des)
    {
      var newMarker = new google.maps.Marker({map: map,position: new google.maps.LatLng(Lat, Long)});
      var infowindow = new google.maps.InfoWindow({content: Des});
      google.maps.event.addListener(newMarker, 'click', function(){infowindow.open(map,marker);});
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
      //marker2.setMap(map);
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
  </script>
  
  <div class="container">
    <div class="jumbotron foot">
      <p>Your coordinates have been uploaded and is being tracked by the FBI</p>
    </div>
  </div>
  
</body>