<%@ page import="hfu.simon.model.*" %>
<%@ page import="java.util.Vector" %>

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
<body class="indigo lighten-3">
	<div id="ticket-grid-wrapper" class="container valign-wrapper center-align">

		<!-- Range-form -->
		<div id="ticket-grid" class="valign card-panel row">

			<%
				// get all needed values for printing the ticket-grid
				Sale saleModel = (Sale) application.getAttribute("saleModel");
				Vector<Ticket> tickets = saleModel.getAllTickets();
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
			</div>

		</div>

	</div>

	<div id="order-collapse-wrapper" class="container">

		<ul class="collapsible" data-collapsible="accordion">
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
						<button type="submit" class="waves-effect waves-light btn">Asuführen</button>
					</form>
				</div>
			</li>
			<li>
				<div class="collapsible-header"><i class="mdi-editor-attach-money"></i>Verkauf eines freien Tickets</div>
				<div class="collapsible-body white left-align">
					<form action="/sale" method="post" class="row">
						<div class="input-field col s6">
							<input id="seat_number_two" type="text">
							<label for="seat_number_two">Sitzplatznummer</label>
						</div>
						<input type="hidden" name="cmd" value="sell"/>
						<button type="submit" class="waves-effect waves-light btn">Asuführen</button>
					</form>
				</div>
			</li>
			<li>
				<div class="collapsible-header"><i class="mdi-editor-attach-money"></i>Verkauf eines reservierten Tickets</div>
				<div class="collapsible-body white center-align">
					<form action="/sale" method="post" class="row">
						<div class="input-field col s6">
							<input id="seat_number_three" type="text" class="validate">
							<label for="seat_number_three">Sitzplatznummer</label>
						</div>
						<div class="input-field col s6">
							<input id="owner_name_three" type="text" class="validate">
							<label for="owner_name_three">Reservierungsname</label>
						</div>
						<input type="hidden" name="cmd" value="unbook"/>
						<button type="submit" class="waves-effect waves-light btn">Asuführen</button>
					</form>
				</div>
			</li>
			<li>
				<div class="collapsible-header"><i class="mdi-alert-warning"></i>Stornierung eines Tickets</div>
				<div class="collapsible-body white left-align">
					<form action="/sale" methode="post" class="row">
						<div class="input-field col s6">
							<input id="seat_number_four" type="text">
							<label for="seat_number_four">Sitzplatznummer</label>
						</div>
						<input type="hidden" name="cmd" value="unsell"/>
						<button type="submit" class="waves-effect waves-light btn">Asuführen</button>
					</form>
				</div>
			</li>
			<li>
				<div class="collapsible-header"><i class="mdi-alert-warning"></i>Reservierungen aufheben</div>
				<div class="collapsible-body white left-align">
					<p>
						Mit dieser Operation werden: alle bestehenden Reservierungen gelöscht, ab sofort Reservierungen unterbunden
					</p>
					<form action="/sale" methode="post" class="row">
						<input type="hidden" name="cmd" value="unbookall"/>
						<button type="submit" class="waves-effect waves-light btn">Asuführen</button>
					</form>
				</div>
			</li>
		</ul>
	</div>

	<!-- Vendor-Scripts -->
	<script src="js/jquery.min.js"></script>
	<script src="js/materialize.min.js"></script>


</body>
</html>