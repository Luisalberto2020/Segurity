function updatePrize(event){
    var price = event.target.dataset.price;
    var quantity = event.target.value;

    event.parentNode.parentNode.parentNode.parentNode.parentNode.querySelector('.span-price').textContent = price  * quantity ;
    console.log(event.parentNode.parentNode.parentNode.parentNode.parentNode);
    
}

function deleteProduct(event){
    var id = event.target.dataset.id;
    var url = event.target.dataset.url;
    var token = event.target.dataset.token;
    var data = {
        'id': id,
        '_token': token
    };
    fetch(url, {
        method: 'POST',
        body: JSON.stringify(data),
        headers:{
            'Content-Type': 'application/json'
        }
    }).then(function(response) {
        return response.json();
    }).then(function(data) {
        if(data.success){
            event.target.parentNode.parentNode.remove();
        }
    });
}


var quantities = document.getElementsByTagName('quantity')
Array.from(quantities).forEach(function(element) {
    element.addEventListener('change', updatePrize)
});

window.addEventListener("DOMContentLoaded", (event) => {
    let quantity = document.getElementById('quantity');
    quantity.addEventListener('change', updatePrize);
    let deleteBtn = document.getElementById('delete');
    deleteBtn.addEventListener('click', deleteProduct);
  });