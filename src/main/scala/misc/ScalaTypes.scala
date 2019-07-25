package misc


trait Thing
trait Vehicle extends Thing
class Automobile extends Vehicle
class Car extends Automobile
class Jeep extends Car
class Coupe extends Car
class Motorcycle extends Vehicle
class Vegetable

class Parking[A](val place: A){
  def parkIt(element: A): Parking[A] = new Parking(element)
}

class CovariantParking[+A](val place: A){
  //Does not compile
  //def parkIt(element: A): CovariantParking[A] = new CovariantParking(element)
  def parkItEasy[B >: A](element: B): CovariantParking[B] = new CovariantParking(element)
}

class NewParking[A](val place1: A, val place2: A)

object ScalaTypes extends App{
  val motorCycleParking = new Parking[Motorcycle](new Motorcycle)
  val jeepParking =  new Parking[Car](new Jeep)
  jeepParking.parkIt(new Coupe)

  //Does not compile
  //val unCompiledParking = new Parking[Motorcycle](new Car)

  //Type inferencing understands that vehicleparking
  val vehicleParking = new Parking(new Car, new Motorcycle,4, 5, 6)
  println(vehicleParking.place)

  val covariantParkingOfCar = new CovariantParking[Car](new Jeep)
  val covariantParkingOfAutomobile = new CovariantParking[Car](new Car)
  covariantParkingOfCar.parkItEasy(new Coupe)
  covariantParkingOfCar.parkItEasy(new Car)
  val automobileParking = covariantParkingOfCar.parkItEasy(new Automobile)
}
