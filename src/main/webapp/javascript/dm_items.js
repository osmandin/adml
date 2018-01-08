// This JS file is temporary and will be replaced when we have a different API.

var asRepo = "2";
var baseURL = "http://159.203.105.249:8089/";
var serverURL="http://104.236.224.175/adml/token"; // change when changing IPs
var token;

$(document).ready(function() {

    $.get( serverURL, function(data){
        token = data;
        // console.log(token);
    }).fail(function() {
        alert("Error doing a lookup.");
    });

    $('#find_in_as').click(function () {
        //e.preventDefault();
        // alert("loaded");
        refid = $('#refID').val();
        var params = "ref_id[]=" + refid;
        getResults(params, refid);
    });

});


// Handle click event on Find in ArchivesSpace button and delegate to getResults function
$('#find_in_as').on('click', function(e) {
    e.preventDefault();
    refid = $('#refID').val();
    var params = "ref_id[]=" + refid;
    getResults(params, refid);
});


// Get JSON results from the AS endpoint
function getResults(data, refid) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("X-ArchivesSpace-Session", token);
        },
        url: baseURL + "/repositories/"+ asRepo +"/find_by_id/archival_objects?",
        data: data,
        success: function(results) {
            // alert("ok: get results");
            if (results["archival_objects"].length < 1) {
                console.log("Sorry, I couldn't find anything for " + refid);
            } else {
                //alert(results["archival_objects"][0]["ref"]);
                getData(results["archival_objects"][0]["ref"]);
            }
        }
    });
}

// Fetches JSON from an ArchivesSpace URI
function getData(uri) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("X-ArchivesSpace-Session", token);
        },
        url: baseURL + uri,
        success: function(data) {
            // alert("ok: get data call")
            if (data["jsonmodel_type"] == "resource") {
                $('#resource').val(data["title"] + ' (' + data["id_0"] + ')');
            } else if (data["jsonmodel_type"] == "archival_object") {
                // alert("archival object")
                $('#component').val(data['display_string'])
                getData(data["resource"]["ref"]);
            }
        }
    });
}