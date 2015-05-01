<%@ page import="hfu.simon.model.*" %>
<%@ page import="java.util.Vector" %>
<%@ page import="hfu.simon.helper.TimedTask" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
	<meta charset="UTF-8">
	<title>Kartenverkauf</title>

	<!-- Styles -->
	<link rel="stylesheet" href="css/normalize.css" media="screen,projection"/>
	<link rel="stylesheet" href="css/materialize.min.css">
	<link rel="stylesheet" href="css/main.css"/>

	<!--Let browser know website is optimized for mobile-->
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

</head>
<body>
	<div id="container-vertical-align" class="container">
		<div id="container-middle">

			<div id="ticket-grid-wrapper" class="row center-align">

				<!-- Ticket-Grid -->
				<div id="ticket-grid" class="card-panel col s6 offset-s3">

					<%
						// get all needed values for printing the ticket-grid
						Sale saleModel = (Sale) application.getAttribute("saleModel");
						Vector<Ticket> tickets = saleModel.getAllTickets();
						boolean saleEnabled = saleModel.isSaleEnabled();
						TimedTask task = saleModel.getTimedTask();
						// calculate boxes
						int ticketsAvailable = saleModel.getTicketCount();
						int rows = ticketsAvailable / 12;
						int index = 1;

						// print 10 tickets in one row and create chunks for each row
						for(int i = 0; i < rows; i++) {

					%>

					<div class="row">

						<%

							// print 10 tickets, with their state (booked, sold) and index
							for(int z = 0; z < 12; z++) { %>

						<div class="col s1 ticket
									<%= (tickets.get(index - 1).getBooked() == 1) ? "booked" : "" %>
									<%= (tickets.get(index - 1).getSold() == 1) ? "sold" : "" %>
								">
							<%= index++ %>
						</div>

						<% } %>

					</div>

					<% 	} %>

					<div id="ticket-map">
						<div class="row">
							<div class="col s4 z-depth-1">frei</div>
							<div class="col s4 z-depth-1 booked">reserviert</div>
							<div class="col s4 z-depth-1 sold">verkauft</div>
						</div>

						<% if(!saleEnabled) { %>
						<div class="z-depth-1">
							<p class="black-text">Zum jetzigen Zeitpunkt können keine Reservierungen mehr angenommen werden</p>
						</div>
						<% } %>
					</div>

				</div>
			</div>

			<!-- Cmd-Forms -->
			<div id="order-collapse-wrapper" class="row left-align">

				<ul class="collapsible popout" data-collapsible="accordion">
					<li>
						<div class="collapsible-header"><i class="mdi-editor-attach-money"></i>Kauf eines freien Tickets</div>
						<div class="collapsible-body white left-align">
							<form action="/sale" method="post" class="row center-align">
								<div class="input-field col s12">
									<input id="seat_number_two" type="text" name="index">
									<label for="seat_number_two">Sitzplatznummer</label>
								</div>
								<input type="hidden" name="cmd" value="sell"/>
								<button type="submit" class="waves-effect waves-light btn">Ausführen</button>
							</form>
						</div>
					</li>
					<% if(saleEnabled) { %>
					<li>
						<div class="collapsible-header"><i class="mdi-action-account-balance-wallet"></i>Reservierung eines Tickets</div>
						<div class="collapsible-body white center-align">
							<form action="/sale" method="post" class="row">
								<div class="input-field col s6">
									<input id="seat_number" type="text" class="validate" name="index">
									<label for="seat_number">Sitzplatznummer</label>
								</div>
								<div class="input-field col s6">
									<input id="owner_name" type="text" class="validate" name="owner">
									<label for="owner_name">Reservierungsname</label>
								</div>
								<input type="hidden" name="cmd" value="book"/>
								<button type="submit" class="waves-effect waves-light btn">Ausführen</button>
							</form>
						</div>
					</li>
					<% } %>
					<li>
						<div class="collapsible-header"><i class="mdi-action-account-balance-wallet"></i>Verkauf eines reservierten Tickets</div>
						<div class="collapsible-body white center-align">
							<form action="/sale" method="post" class="row">
								<div class="input-field col s6">
									<input id="seat_number_three" type="text" class="validate" name="index">
									<label for="seat_number_three">Sitzplatznummer</label>
								</div>
								<div class="input-field col s6">
									<input id="owner_name_three" type="text" class="validate" name="owner">
									<label for="owner_name_three">Reservierungsname</label>
								</div>
								<input type="hidden" name="cmd" value="unbook"/>
								<button type="submit" class="waves-effect waves-light btn">Ausführen</button>
							</form>
						</div>
					</li>
					<li>
						<div class="collapsible-header"><i class="mdi-alert-warning"></i>Stornierung eines Tickets</div>
						<div class="collapsible-body white left-align">
							<form action="/sale" method="post" class="row center-align">
								<div class="input-field col s12">
									<input id="seat_number_four" type="text" name="index">
									<label for="seat_number_four">Sitzplatznummer</label>
								</div>
								<input type="hidden" name="cmd" value="unsell"/>
								<button type="submit" class="waves-effect waves-light btn">Ausführen</button>
							</form>
						</div>
					</li>
					<li>
						<div class="collapsible-header">
							<i class="mdi-alert-warning"></i>Reservierungen aufheben

							<!-- Does our model have a TimedTask? -->
							<% if(task != null) { %>
								<span class="badge">Zeitgesteuerte Deaktivierung: <%= task.getExecutionTime() %></span>
							<% } %>
						</div>
						<div class="collapsible-body white center-align">
							<div class="col s6">
								<p>
									Mit dieser Operation werden alle bestehenden Reservierungen gelöscht und ab sofort Reservierungen unterbunden/erneut aufgenommen
								</p>
								<form action="/sale" method="post" class="row">
									<input type="hidden" name="cmd" value="unbookall"/>

									<% if(saleEnabled) { %>
									<button type="submit" class="waves-effect waves-light btn btn2">Sofort deaktivieren</button>
									<% } else { %>
									<button type="submit" class="waves-effect waves-light btn">Sofort aktivieren</button>
									<% } %>

								</form>
							</div>
							<div class="col s6">
								<form action="/sale" method="post" class="row">
									<div class="input-field col s6 custom-label">
										Deaktiviere Reservierungen am:
									</div>
									<div class="input-field col s3">
										<input id="date" type="date" class="datepicker" name="date">
									</div>
									<div id="time" class="input-field col s3">
										<input type="time" class="validate" name="time"><span>Uhr</span>
										<!--<label for="time">Vor Beginn deaktivieren in Minuten</label>-->
									</div>
									<input type="hidden" name="cmd" value="disablebooking"/>

									<button type="submit" class="waves-effect waves-light btn btn2 custom-button">Zeitgesteuert deaktivieren</button>
								</form>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>

	<!-- Error-Message -->
	<% if(request.getAttribute("error") != null) { %>
	<div id="error-msg-wrapper">
		<div class="card-panel z-depth-1 valign-wrapper">
			<div class="valign white-text">
				<i class="small mdi-alert-error"></i> Fehler: <%= request.getAttribute("error") %>
			</div>
		</div>
	</div>
	<% } %>

	<!-- Success-Message -->
	<% if(request.getAttribute("success") != null) { %>
	<div id="success-msg-wrapper">
		<div class="card-panel z-depth-1 valign-wrapper">
			<div class="valign white-text">
				<i class="small mdi-navigation-check"></i> Operation erfolgreich ausgeführt
			</div>
		</div>
	</div>
	<% } %>

	<!-- Vendor-Scripts -->
	<script src="js/jquery.min.js"></script>
	<script src="js/materialize.js"></script>

	<!-- Custom-scripts -->
	<script>
		$('.datepicker').pickadate({
			selectMonths: true, // Creates a dropdown to control month
			selectYears: 15 // Creates a dropdown of 15 years to control year
		});
	</script>

</body>
</html>