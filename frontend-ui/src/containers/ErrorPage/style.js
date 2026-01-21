import styled from "styled-components";

export const Container = styled.div`

  /*======================
      404 page
  =======================*/


  .page_404 {
    font-family: 'Lato';
    padding: 40px 0;
    background: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
  }


  .four_zero_four_bg {
    background-repeat: no-repeat;
    background-image: url(https://cdn.dribbble.com/users/285475/screenshots/2083086/dribbble_1.gif);
    width: 100%;
    height: 400px;
    background-position: center;
  }


  h1 {
    font-size: 80px;
    line-height: 0;
    text-align: center;
  }

  .four_zero_four_bg h3 {
    font-size: 80px;
  }

  .link_404 {
    color: #fff !important;
    padding: 10px 20px;
    background: #39ac31;
    margin: 20px 0;
    display: inline-block;
  }

  .contant_box_404 {
    text-align: center;
    margin-top: -50px;
  }

`