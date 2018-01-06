// Generate date variables
var date = new Date();
var day = date.getDate();
var month = date.getMonth() + 1;
var year = date.getFullYear();
if (month < 10) month = "0" + month;
if (day < 10) day = "0" + day;
var today = year + "-" + month + "-" + day;
var asRepo = "2";
var baseURL = "http://localhost:8089/"
var token="4dd2847ad4d47ce8eba9c8f613484acc4a462547b5248b7ce2ea7f19f0f26c0b"


$(document).ready(function() {
    $('#find_in_as').click(function () {
        //e.preventDefault();
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

// Populate auto id field with unique ID
function generateId() {
    if ($('#dm_item_auto_id').val() == "") {
        var auto_id = createId();
        $('#dm_item_auto_id').val(auto_id);
    }
}

// Generate a unique ID
function createId() {
    var text = "";
    var possible = "abcdefghijklmnopqrstuvwxyz0123456789";
    for( var i=0; i < 10; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    return text;
}

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
            // alert("success");
            if (results["archival_objects"].length < 1) {
                console.log("Sorry, I couldn't find anything for " + refid);
            } else {
                // alert(results["archival_objects"][0]["ref"]);
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
            // alert("success")
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

function validateForm() {
    var isValid = true;
    $('.dm_items .form-control[required]').each(function() {
        if ( $(this).val() == ''  ) {
            isValid = false;
        }
    });
    return isValid;
}