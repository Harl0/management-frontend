/**
 * Created by jason on 04/07/17.
 */
$(document).ready(function () {

    deleteFunction = function (id) {


        if (confirm('Are you sure you want to delete client: ' + id + '')) {
            $.get('http://mgmt-www/client/deleteClient?_id=' + id, function (data) {
                console.log("Deleted client: " + id);
            });
        }
        window.location.replace("http://mgmt-www/client/retrieveClientList");
    }
});