<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>Todo List</title>

  <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />

  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />

  <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js" rel="stylesheet" />

  <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js" rel="stylesheet" />

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" />

  <link rel="stylesheet" type="text/css" href="./css/style.css" />
  <link rel="stylesheet" type="text/css" href="./css/todo.css" />

</head>

<body>
  <jsp:include page="components/header.jsp" />
  <main class="container-todo d-flex justify-content-center">
    <div class="col-md-8">
      <div class="card-hover-shadow-2x mb-3 card">
        <div class="card-header-tab card-header d-flex align-items-center justify-content-between">
          <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
            <i class="fa fa-tasks"></i>&nbsp;Task Lists
          </div>
          <form class="d-flex">
            <div class="form-outline">
              <textarea type="text" style="height: 1.2em; width: 40vw" id="form2" class="form-control" placeholder="New task..."></textarea>
            </div>
            <button class="btn btn-danger ms-2 btn-sm" type="submit">Add</button>
          </form>
        </div>
        <div class="scroll-area-sm">
          <perfect-scrollbar class="ps-show-limits">
            <div style="position: static" class="ps ps--active-y">
              <div class="ps-content">
                <ul class="list-group list-group-flush">
                  <li class="list-group-item">
                    <div class="todo-indicator bg-warning"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox12" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox12">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left">
                          <div class="widget-heading">
                            Call Sam For payments
                            <div class="badge badge-danger ml-2">
                              Rejected
                            </div>
                          </div>
                          <div class="widget-subheading"><i>By Bob</i></div>
                        </div>
                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>


                  <li class="list-group-item">
                    <div class="todo-indicator bg-focus"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox1" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox1">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left">
                          <div class="widget-heading">
                            Make payment to Bluedart
                          </div>
                          <div class="widget-subheading">
                            <div>
                              By Johnny
                              <div class="badge badge-pill badge-info ml-2">
                                NEW
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>


                  <li class="list-group-item">
                    <div class="todo-indicator bg-primary"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox4" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox4">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left flex2">
                          <div class="widget-heading">Office rent</div>
                          <div class="widget-subheading">By Samino!</div>
                        </div>

                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>

                  <li class="list-group-item">
                    <div class="todo-indicator bg-warning"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox12" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox12">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left">
                          <div class="widget-heading">
                            Call Sam For payments
                            <div class="badge badge-danger ml-2">
                              Rejected
                            </div>
                          </div>
                          <div class="widget-subheading"><i>By Bob</i></div>
                        </div>
                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>



                  <li class="list-group-item">
                    <div class="todo-indicator bg-info"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox2" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox2">&nbsp;</label>
                          </div>
                        </div>

                        <div class="widget-content-left">
                          <div class="widget-heading">
                            Office grocery shopping
                          </div>
                          <div class="widget-subheading">By Tida</div>
                        </div>
                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>


                  <li class="list-group-item">
                    <div class="todo-indicator bg-warning"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox12" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox12">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left">
                          <div class="widget-heading">
                            Call Sam For payments
                            <div class="badge badge-danger ml-2">
                              Rejected
                            </div>
                          </div>
                          <div class="widget-subheading"><i>By Bob</i></div>
                        </div>
                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>

                  <li class="list-group-item">
                    <div class="todo-indicator bg-success"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox3" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox3">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left flex2">
                          <div class="widget-heading">
                            Ask for Lunch to Clients
                          </div>
                          <div class="widget-subheading">By Office Admin</div>
                        </div>

                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>

                  <li class="list-group-item">
                    <div class="todo-indicator bg-success"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox3" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox3">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left flex2">
                          <div class="widget-heading">
                            Ask for Lunch to Clients
                          </div>
                          <div class="widget-subheading">By Office Admin</div>
                        </div>

                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>

                  <li class="list-group-item">
                    <div class="todo-indicator bg-success"></div>
                    <div class="widget-content p-0">
                      <div class="widget-content-wrapper">
                        <div class="widget-content-left mr-2">
                          <div class="custom-checkbox custom-control">
                            <input class="custom-control-input" id="exampleCustomCheckbox10" type="checkbox" /><label
                              class="custom-control-label" for="exampleCustomCheckbox10">&nbsp;</label>
                          </div>
                        </div>
                        <div class="widget-content-left flex2">
                          <div class="widget-heading">
                            Client Meeting at 11 AM
                          </div>
                          <div class="widget-subheading">By CEO</div>
                        </div>

                        <div class="widget-content-right">
                          <button class="border-0 btn-transition btn btn-outline-success">
                            <i class="fa fa-check"></i>
                          </button>
                          <button class="border-0 btn-transition btn btn-outline-danger">
                            <i class="fa fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </perfect-scrollbar>
        </div>
        <div class="d-block p-2 text-right card-footer">
          <a href="project.jsp" class="mr-2 btn btn-primary btn-sm">Cancel</a>
        </div>
      </div>
    </div>
  </main>
  <jsp:include page="components/footer.jsp" />
</body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
  integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
  integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
  integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>

</html>