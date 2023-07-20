var commands = [];
trList = document.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
for(var i=0; i<trList.length;i++){
    tdList = trList[i].getElementsByTagName("td");
    var command = tdList[0].textContent;
    var value = tdList[2].textContent;
	var element_name = tdList[3].textContent;
	var screen_name = tdList[4].textContent;
    
    var options = [];
    var optionList = tdList[1].getElementsByTagName("option");
    for(var j=0;j<optionList.length;j++){
        options.push(optionList[j].textContent);
    }
    commands.push([command, options, value, element_name, screen_name]);
}
return commands;