function getToken() {
    let cookies = document.cookie.split(";");
    let token = "";
    let cookieExists = false;
    while(!cookieExists || cookies.length > 0){
        let cookie = cookies.pop();
        if(cookie.includes("token")){
            token = cookie.split("=")[1];
            cookieExists = true;
        }
    }
    return token;
}