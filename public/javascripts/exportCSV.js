/**
 * Created by jason on 13/07/17.
 */
$(document).ready(function () {
    $("#csv-submit").click(function () {
        var collectorIdArray = [];
        var collectorNameArray = [];


        var collectorIdGeneration = $('#collectorId').filter(function () {
            $('[id="' + this.id + '"]').each(function(){
                var collectorIdValue = $(this).text();
                if(collectorIdValue.length > 0){
                    collectorIdArray.push(collectorIdValue);
                } else {
                    collectorIdArray.push(" ");
                }
            });
        });

        var collectorNameGeneration = $('#collectorName').filter(function () {
            $('[id="' + this.id + '"]').each(function(){
                var collectorNameValue = $(this).text();
                if(collectorNameValue.length > 0){
                    collectorNameArray.push(collectorNameValue);
                } else {
                    collectorNameArray.push(" ");
                }
            });
        });

        var data = [];
        data += ["Collector ID", "Collector Name"];

        collectorIdArray.forEach(function(infoArray, index){
            data += collectorIdArray[index] + ",";
            data += collectorNameArray[index] + ",";
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
