/**
 * Created by jason on 04/07/17.
 */
$(document).ready(function () {

    deleteFunction = function (id, clientName, clientId) {
        if (confirm('Are you sure you want to delete client: ' + clientName + ' with ID ' + clientId + '')) {
            $.get('http://mgmt.local.basgov.uk/management/client/delete?_id=' + id, function (data) {
            });
        }
        window.location.replace("http://mgmt.local.basgov.uk/management/client/list");
    }
});