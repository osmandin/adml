var asRepo = "2"; // ASpace repo

//var baseURL="https://archivesspace-staff.mit.edu/api"
var baseURL="https://arcsubmit-stage.mit.edu/adml/api";  //proxy
//var baseURL = "http://159.203.105.249:8089/"; // ASpace test server URL
// var baseURL = "http://localhost:8089" // ASpace URL
//var baseURL="https://emmastaff-lib.mit.edu/api"; // production IASC url

var serverURL="https://arcsubmit-stage.mit.edu/adml/token"; // change when changing IPs
//var serverURL="https://iasc.mit.edu/adml/token"; // change when changing IPs
//var serverURL="http://localhost:8080/adml/token"; // for local machine testing
var token;

$(document).ready(function() {
    $.get( serverURL, function(data){
        token = data;
        console.log("Got token from ADML server OK.");
    }).fail(function() {
        alert("Error doing a lookup.");
    });

    $('#find_in_as').click(function () {
        refid = $('#refID').val();
        var params = "ref_id[]=" + refid;
        getResults(params, refid);
    });
});


// Handle click event on "Find in ArchivesSpace" button
$('#find_in_as').on('click', function(e) {
    e.preventDefault();
    refid = $('#refID').val();
    var params = "ref_id[]=" + refid;
    getResults(params, refid);
});


// Fetches archival object ID by ref ID
function getResults(data, refid) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("X-ArchivesSpace-Session", token);
            $('#box').val("");
            $('#component').val("");
            $('#resource').val("");
            console.log(token);
        },
        url: baseURL + "/repositories/"+ asRepo +"/find_by_id/archival_objects?",
        data: data,
        success: function(results) {
            console.log("Fetched archival object by id");
            if (results["archival_objects"].length < 1) {
                console.log("Sorry, I couldn't find anything for: " + refid);
            } else {
                getData(results["archival_objects"][0]["ref"]);
            }
        },  error: function (xhr, status) {
            console.log('Ajax error = ' + xhr.statusText);
        }
    });
}

// Fetches component and resource data and box info or a particular ref ID
function getData(uri) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            console.log("Sending request to:" + baseURL + uri);
            request.setRequestHeader("X-ArchivesSpace-Session", token);
            request.setRequestHeader('Accept','application/json; charset=utf-8');
        },
        url: baseURL + uri,
        success: function(data) {
            console.log("Entering getData()");
            if (data["jsonmodel_type"] == "resource") {
                console.log("This is a resource");
                $('#resource').val(data["title"] + ' (' + data["id_0"] + ')');

            } else if (data["jsonmodel_type"] == "archival_object") {
                console.log("This is an archival object");
                $('#component').val(data['display_string'])
                console.log("Getting info regarding resource ref");
                getData(data["resource"]["ref"]);

                var i;
                for (i = 0; i < data["instances"].length; i++) {
                    if (data["instances"][i]["instance_type"] == "computer_disks") {
                        getBoxes(data["instances"][i]["sub_container"]["top_container"]["ref"]);
                    }
                }
            } else if (data["type"] == "adml_resource") {
                $('#resource').val(data["title"]);
            } else {
                console.log("Unknown json model type");
            }

        }, error: function (xhr, status) {
            console.log('Error in getData = ' + xhr.statusText);
            console.log(status);
            console.log(xhr.responseText);

        }
    });
}




// Fetches box information through top container (top container should be tied to "computer_disks"
function getBoxes(data) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("X-ArchivesSpace-Session", token);
            console.log("Sending request to top_container:" + baseURL + data);

        },
        url: baseURL + data,
        success: function(results) {
            if (results["indicator"].length < 1) {
                console.log("Sorry, I couldn't find box info for: " + data);
                console.log(results);
            } else {
                $('#box').val(results["indicator"]);
            }
        },  error: function (xhr, status) {
            console.log('Ajax error = ' + xhr.statusText);
        }
    });
}