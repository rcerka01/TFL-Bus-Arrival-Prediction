// redirect to mobile
 if (screen.width <= 800) {
    window.location = "http://mobiletfl.raitis.co.uk";
  }





// get busstops
function getStops() {

 $.get('/getStops', {'nr': $("#busnr").val() }, function(data) {

                     var options = $("#busstop");

                     // make sure "busstop" tag is empty
                     options.empty();

                     $.each(data, function() {
                        // each option has id, lat, lang values
                        options.append(new Option( this.name, [this.id, this.lat, this.lon ] ));
                     });

                     // show select "busstop" tag
                     $( "#part2" ).show();

                 });
 }




// predict arrival
function predict(resultId, busstopId, busNr) {

 // distinguish saved jobs from new
 var st = "";
 var nr;
 if (busstopId) {
  st = busstopId;
  nr = busNr;}
 else {
  st = $("#busstop").val();
  nr = $("#busnr").val();
  resultId= ""; }

 var station = st.split(',');

 $("#result" + resultId + " ul").empty();
 $("#map").show();

 $.get('/predict', {'nr': nr, 'st': station[0]}, function(data) {

                      // sorting json
                      data = data.sort(sortByProperty('timeToStation'));

                      run = true;
                      time = "";

                      $.each(data, function() {

                        // change time to "Approaching"
                        if (this.timeToStation < 120)
                           time = "<strong>Approaching</strong>";
                        else
                           time = secToMin(this.timeToStation);

                        $("#result" + resultId + " ul").append('<li>' + time + '</li>');

                        // map update executed only once
                        if (run) {
                          initMap(parseFloat(station[1]), parseFloat(station[2]));
                          run = false;
                        }
                      });

                      // if no data
                      if ($.isEmptyObject(data)) {
                        $("#result" + resultId + " ul").append('<li>No Data</li>');
                        $("#map").hide();
                      }

                  });

$("#result" + resultId).show();
}



// save new route
function saveRoute() {
  var nr = $("#busnr").val();
  var stId = $("#busstop").val().split(',')[0];
  var stName = $("select[name='busstop'] option:selected").text();

  $.get('/saveRoute', { 'nr': nr, 'stId': stId, 'stName': stName }, function(data) {
       bootbox.alert("Route saved.", function() {
         getSavedJobs();
       });
    });
}




// get saved routes
function getSavedJobs() {
  $("#savedjobstable").empty();
  $.get('/getSavedJobs', { }, function(data) {
   $.each(data, function() {
     $('#savedjobstable').append("<tr><td><button type='button' onclick='deleteJob(" + this.id +
       ");' class='btn-xs btn btn-primary'>Delete</button></td><td><button type='button' onclick=predict('" + this.id +
       "','" + this.busstopid + "','" + this.busnumber +
       "');$('#result').hide(); class='btn-xs btn btn-primary'>Update Prediction</button></td><td><strong>" + this.busnumber +
       "</strong></td><td>" + this.busstopname + "</td><td><div id='result" + this.id +
       "' name='result" + this.id +
       "' class='alert alert-info' style='width:16em;'><strong>Next:</strong><ul></ul></div></td></tr>");
       predict(this.id, this.busstopid, this.busnumber);
     });
  });
}




// delete route
function deleteJob(id) {
  bootbox.confirm("Are you sure want to delete this route?", function(result) {
   if(result) {
      $.get('/deleteJob', { 'id': id }, function(data) {
        getSavedJobs();
      });
    }
  });
}




// convert seconds to minutes
function secToMin(sec) {
 var hr = Math.floor(sec / 3600);
 var min = Math.floor((sec - (hr * 3600))/60);
 sec -= ((hr * 3600) + (min * 60));
 sec += ''; min += '';
 while (min.length < 2) {min = '0' + min;}
 while (sec.length < 2) {sec = '0' + sec;}
 hr = (hr)?':'+hr:'';
 return hr + min + ' minutes ' + sec + ' seconds';
}



// display map
function initMap(lat, lng) {
 if (lat && lng) {
  var myLatLng = {lat: lat, lng: lng};
  var map = new google.maps.Map(document.getElementById('map'), {
    zoom: 15,
    center: myLatLng
  });
  var marker = new google.maps.Marker({
    position: myLatLng,
    map: map,
  });
 }
}



// sort Json object
var sortByProperty = function (property) {
    return function (x, y) {
        return ((x[property] === y[property]) ? 0 : ((x[property] > y[property]) ? 1 : -1));
    };
};



// display login alert
function loginAlert() {
  bootbox.alert('You have to <a href="login">Login</a> to use this option.');
}