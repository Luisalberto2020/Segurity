var   nItems = 2;


function addItem(){

    var html = ""
    html += '<div class="row mt-1">'
    html += '<div class="col">'
    html += '<input type="text" name="id-item'+nItems+'" class="form-control item-form" placeholder="id item" />'
    html += '</div>'
    html += '<div class="col">'
    html += '<input type="text" name="nombre-item'+nItems+'" class="form-control item-form" placeholder="nombre" />'
    html += '</div>'
    html += '<div class="col">'
    html += '<input type="number" name="cantidad-item'+nItems+'" class="form-control item-form" placeholder="cantidad" />'
    html += '</div>'
    html += '</div>'

    
    document.getElementById("div-productos").innerHTML += html;
    nItems++;
    
}

function sendForm(){
var items = document.getElementsByClassName("item-form");
var name = document.getElementById("name").value;
var price = document.getElementById("price").value;
var seller = document.getElementById("seller").value;

if (name == "" || price == ""){
    alert("Faltan datos");
    return;
}
if (items.length == 0){
    alert("Faltan datos");
    return;
}
if (seller == undefined){
    seller =  0;
}

var json = '{ "name": "'+name+'", "price": '+price+',sell:'+ seller+ ', "products": [';
for(var i = 0; i < items.length; i++){
    if(i != 0){
        json += ',';
    }
    json += '{ "id": "'+items[i].value+'", "name": "'+items[i+1].value+'", "quantity": '+items[i+2].value+'}';
    i += 2;
}
json += ']}';
var base64 = btoa(json);


const xhr = new XMLHttpRequest();
xhr.open('POST', '/admin/additem');
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhr.setRequestHeader('Bearer', getToken());
const data = 'data='+base64;
try{
    xhr.send(data);
}catch(e){
    alert("Error al enviar la solicitud");
    console.log(e);
    return;
}
document.getElementById("alertS").classList.remove("invisible");





}

