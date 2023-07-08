function updatePrize(event){
    var price = event.target.dataset.price;
    var quantity = event.target.value;
    var spanPrice = event.target.parentNode.parentNode.parentNode.parentNode.querySelector('span.span-price');
    spanPrice.innerHTML = price * quantity;


    
}

function deleteProduct(event){
    var id = event.target.dataset.id;
    var url = '/admin/products/delete'
    var token = getToken();
    var data = {
        'id': id,
        'token': token
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
            alert("Producto eliminado correctamente");
        }
    });
}


var quantities = document.getElementsByTagName('quantity')
Array.from(quantities).forEach(function(element) {
    element.addEventListener('change', updatePrize)
});

window.addEventListener("DOMContentLoaded", (event) => {
   let quantities = document.getElementsByClassName('quantity')
    Array.from(quantities).forEach(function(element) {
        element.addEventListener('change', updatePrize)
    });


    let deleteButtons = document.getElementsByClassName('delete-product');
    Array.from(deleteButtons).forEach(function(element) {
        element.addEventListener('click', deleteProduct)
    }
    );
  });