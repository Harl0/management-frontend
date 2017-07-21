/**
 * Created by jason on 13/07/17.
 */
$(document).ready(function () {
    $("#csv-submit").click(function () {
        var clientIdArray = [];
        var clientNameArray = [];


        var clientIdGeneration = $('#clientId').filter(function () {
            $('[id="' + this.id + '"]').each(function(){
                var clientIdValue = $(this).text();
                if(clientIdValue.length > 0){
                    clientIdArray.push(clientIdValue);
                } else {
                    clientIdArray.push(" ");
                }
            });
        });

        var clientNameGeneration = $('#clientName').filter(function () {
            $('[id="' + this.id + '"]').each(function(){
                var clientNameValue = $(this).text();
                if(clientNameValue.length > 0){
                    clientNameArray.push(clientNameValue);
                } else {
                    clientNameArray.push(" ");
                }
            });
        });

        var data = [];
        data += ["Client ID", "Client Name"];

        clientIdArray.forEach(function(infoArray, index){
            data += clientIdArray[index] + ",";
            data += clientNameArray[index] + ",";
        });

            var today = new Date();
            var date = today.getDate()+'-'+(today.getMonth()+1)+'-'+today.getFullYear();
            var csvContent = "data:text/csv;charset=utf-8," + data;
            var fileName = "CollectorList-" + date + ".csv";
            var encodedUri = encodeURI(csvContent);
            var link = document.createElement("a");
            link.setAttribute("href", encodedUri);
            link.setAttribute("download", fileName);
            document.body.appendChild(link);
            link.click();
    });
});
