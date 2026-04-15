package com.agmdesarrollos.geopix.maestro;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogosDANE {

    public record DepartamentoDTO(String codigo, String nombre) {}
    public record MunicipioDTO(String codigo, String nombre, String departamentoCodigo) {}

    private final Map<String, String> departamentos;
    private final Map<String, List<String>> municipiosPorDepartamento;

    public CatalogosDANE() {
        this.departamentos = new LinkedHashMap<>();
        this.municipiosPorDepartamento = new LinkedHashMap<>();

        departamentos.put("05", "Antioquia");
        municipiosPorDepartamento.put("05", Arrays.asList(
            "Medellín", "Abejorral", "Abriaquí", "Alejandría", "Amagá", "Amalfi", "Andes",
            "Angelópolis", "Angostura", "Anorí", "Apartadó", "Arboletes", "Argelia",
            "Armenia", "Barbosa", "Bello", "Belmira", "Betania", "Betulia", "Briceño",
            "Buriticá", "Cáceres", "Caicedo", "Caldas", "Campamento", "Cañasgordas",
            "Caracolí", "Caramanta", "Carepa", "Carolina del Príncipe", "Caucasia", "Chigorodó",
            "Cisneros", "Cocorná", "Concepción", "Concordia", "Copacabana", "Dabeiba",
            "Donmatías", "Ebéjico", "El Bagre", "Entrerríos", "Envigado", "Fredonia",
            "Frontino", "Giraldo", "Girardota", "Gómez Plata", "Granada", "Guadalupe",
            "Guarne", "Guatapé", "Heliconia", "Hispania", "Itagüí", "Ituango",
            "Jardín", "Jericó", "La Ceja", "La Estrella", "La Pintada", "La Unión",
            "Liborina", "Maceo", "Marinilla", "Montebello", "Murindó", "Mutatá",
            "Nariño", "Nechí", "Necoclí", "Olaya", "Peñol", "Peque", "Pueblorrico",
            "Puerto Berrío", "Puerto Nare", "Puerto Triunfo", "Remedios", "Retiro",
            "Rionegro", "Sabanalarga", "Sabaneta", "Salgar", "San Andrés de Cuerquia",
            "San Carlos", "San Francisco", "San Jerónimo", "San José de la Montaña",
            "San Juan de Urabá", "San Luis", "San Pedro de los Milagros", "San Pedro de Urabá",
            "San Rafael", "San Roque", "San Vicente Ferrer", "Santa Bárbara", "Santa Rosa de Osos",
            "Santo Domingo", "Segovia", "Sonsón", "Sopetrán", "Támesis", "Tarazá", "Tarso",
            "Titiribí", "Toledo", "Turbo", "Uramita", "Urrao", "Valdivia", "Valparaíso",
            "Vegachí", "Venecia", "Vigía del Fuerte", "Yalí", "Yarumal", "Yolombó",
            "Yondó", "Zaragoza"
        ));

        departamentos.put("08", "Atlántico");
        municipiosPorDepartamento.put("08", Arrays.asList(
            "Barranquilla", "Baranoa", "Campo de la Cruz", "Candelaria", "Galapa",
            "Juan de Acosta", "Luruaco", "Malambo", "Manatí", "Palmar de Varela",
            "Piojó", "Polonuevo", "Ponedera", "Puerto Colombia", "Repelón", "Sabanagrande",
            "Sabanalarga", "Santa Lucía", "Santo Tomás", "Soledad", "Suán", "Tubará", "Usiacurí"
        ));

        departamentos.put("11", "Bogotá D.C.");
        municipiosPorDepartamento.put("11", Arrays.asList("Bogotá D.C."));

        departamentos.put("13", "Bolívar");
        municipiosPorDepartamento.put("13", Arrays.asList(
            "Cartagena", "Achí", "Altos del Rosario", "Arenal", "Arjona", "Arroyohondo",
            "Barranco de Loba", "Calamar", "Cantagallo", "Cicuco", "Clemencia", "Córdoba",
            "El Carmen de Bolívar", "El Guamo", "El Peñón", "Hatillo de Loba", "Magangué",
            "Mahates", "Margarita", "María la Baja", "Mompós", "Montecristo", "Morales",
            "Norosí", "Pinillos", "Regidor", "Río Viejo", "San Cristóbal", "San Estanislao",
            "San Fernando", "San Jacinto", "San Jacinto del Cauca", "San Juan Nepomuceno",
            "San Martín de Loba", "San Pablo", "Santa Catalina", "Santa Rosa",
            "Santa Rosa del Sur", "Simití", "Soplaviento", "Talaigua Nuevo", "Tiquisio",
            "Turbaco", "Turbaná", "Villanueva", "Zambrano"
        ));

        departamentos.put("15", "Boyacá");
        municipiosPorDepartamento.put("15", Arrays.asList(
            "Tunja", "Almeida", "Aquitania", "Arcabuco", "Belén", "Berbeo", "Betéitiva",
            "Boavita", "Boyacá", "Briceño", "Buenavista", "Busbanzá", "Caldas", "Campohermoso",
            "Cerinza", "Chinavita", "Chiquinquirá", "Chíquiza", "Chiscas", "Chita",
            "Chitaraque", "Chivatá", "Chivor", "Ciénega", "Cómbita", "Coper", "Corrales",
            "Covarachía", "Cubará", "Cucaita", "Cuítiva", "Duitama", "El Cocuy", "El Espino",
            "Firavitoba", "Floresta", "Gachantivá", "Gámeza", "Garagoa", "Guacamayas",
            "Guateque", "Guayatá", "Güicán", "Iza", "Jenesano", "Jericó", "La Capilla",
            "La Uvita", "La Victoria", "Labranzagrande", "Macanal", "Maripí", "Miraflores",
            "Mongua", "Monguí", "Moniquirá", "Motavita", "Muzo", "Nobsa", "Nuevo Colón",
            "Oicatá", "Otanche", "Pachavita", "Páez", "Paipa", "Pajarito", "Panqueba",
            "Pauna", "Paya", "Paz de Río", "Pesca", "Pisba", "Puerto Boyacá", "Quípama",
            "Ramiriquí", "Ráquira", "Rondón", "Saboyá", "Sáchica", "Samacá", "San Eduardo",
            "San José de Pare", "San Luis de Gaceno", "San Mateo", "San Miguel de Sema",
            "San Pablo de Borbur", "Santa María", "Santa Rosa de Viterbo", "Santa Sofía",
            "Santana", "Sativanorte", "Sativasur", "Siachoque", "Soatá", "Socha", "Socotá",
            "Sogamoso", "Somondoco", "Sora", "Soracá", "Sotaquirá", "Susacón", "Sutamarchán",
            "Sutatenza", "Tasco", "Tenza", "Tibaná", "Tibasosa", "Tinjacá", "Tipacoque",
            "Toca", "Togüí", "Tópaga", "Tota", "Tununguá", "Turmequé", "Tuta", "Tutazá",
            "Úmbita", "Ventaquemada", "Villa de Leyva", "Viracachá", "Zetaquira"
        ));

        departamentos.put("17", "Caldas");
        municipiosPorDepartamento.put("17", Arrays.asList(
            "Manizales", "Aguadas", "Anserma", "Aranzazu", "Belalcázar", "Chinchiná",
            "Filadelfia", "La Dorada", "La Merced", "Manzanares", "Marmato", "Marquetalia",
            "Marulanda", "Neira", "Norcasia", "Pácora", "Palestina", "Pensilvania",
            "Riosucio", "Risaralda", "Salamina", "Samaná", "San José", "Supía", "Victoria",
            "Villamaría", "Viterbo"
        ));

        departamentos.put("18", "Caquetá");
        municipiosPorDepartamento.put("18", Arrays.asList(
            "Florencia", "Albania", "Belén de los Andaquíes", "Cartagena del Chairá",
            "Curillo", "El Doncello", "El Paujil", "La Montañita", "Milán", "Morelia",
            "Puerto Rico", "San José del Fragua", "San Vicente del Caguán", "Solano",
            "Solita", "Valparaíso"
        ));

        departamentos.put("19", "Casanare");
        municipiosPorDepartamento.put("19", Arrays.asList(
            "Yopal", "Aguazul", "Chámeza", "Hato Corozal", "La Salina", "Maní",
            "Monterrey", "Nunchía", "Orocué", "Paz de Ariporo", "Pore", "Recetor",
            "Sabanalarga", "Sácama", "San Luis de Palenque", "Támara", "Tauramena",
            "Trinidad", "Villanueva"
        ));

        departamentos.put("20", "Cauca");
        municipiosPorDepartamento.put("20", Arrays.asList(
            "Popayán", "Almaguer", "Argelia", "Balboa", "Bolívar", "Buenos Aires",
            "Cajibío", "Caldono", "Caloto", "Corinto", "El Tambo", "Florencia",
            "Guachené", "Guapi", "Inzá", "Jambaló", "La Sierra", "La Vega",
            "López de Micay", "Mercaderes", "Miranda", "Morales", "Padilla", "Páez",
            "Patía", "Piamonte", "Piendamó", "Puerto Tejada", "Puracé", "Rosas",
            "San Sebastián", "Santander de Quilichao", "Santa Rosa", "Silvia", "Sotará",
            "Suárez", "Sucré", "Timbío", "Timbiquí", "Toribío", "Totoró", "Villa Rica"
        ));

        departamentos.put("23", "Cesar");
        municipiosPorDepartamento.put("23", Arrays.asList(
            "Valledupar", "Aguachica", "Agustín Codazzi", "Astrea", "Becerril", "Bosconia",
            "Chimichagua", "Chiriguaná", "Curumaní", "El Copey", "El Paso", "Gamarra",
            "González", "La Gloria", "La Jagua de Ibirico", "La Paz", "Manaure Balcón del Cesar",
            "Pailitas", "Pelaya", "Pueblo Bello", "Río de Oro", "San Alberto", "San Diego",
            "San Martín", "Tamalameque"
        ));

        departamentos.put("25", "Cundinamarca");
        municipiosPorDepartamento.put("25", Arrays.asList(
            "Bogotá", "Agua de Dios", "Albán", "Anapoima", "Anolaima", "Apulo", "Arbeláez",
            "Beltrán", "Bituima", "Bojacá", "Cabrera", "Cachipay", "Cajicá", "Caparrapí",
            "Cáqueza", "Carmen de Carupa", "Chaguaní", "Chía", "Chipaque", "Choachí",
            "Chocontá", "Cogua", "Cota", "Cucunubá", "El Colegio", "El Peñón", "El Rosal",
            "Facatativá", "Fómeque", "Fosca", "Funza", "Fúquene", "Fusagasugá", "Gachalá",
            "Gachancipá", "Gachetá", "Gama", "Girardot", "Granada", "Guachetá", "Guaduas",
            "Guasca", "Guataquí", "Guatavita", "Guayabal de Síquima", "Guayabetal", "Gutiérrez",
            "Jerusalén", "Junín", "La Calera", "La Mesa", "La Palma", "La Peña", "La Vega",
            "Lenguazaque", "Machetá", "Madrid", "Manta", "Medina", "Mosquera", "Nariño",
            "Nemocón", "Nilo", "Nimaima", "Nocaima", "Pacho", "Paime", "Pandi", "Paratebueno",
            "Pasca", "Puerto Salgar", "Pulí", "Quebradanegra", "Quetame", "Quipile", "Ricaurte",
            "San Antonio del Tequendama", "San Bernardo", "San Cayetano", "San Francisco",
            "San Juan de Rioseco", "Sasaima", "Sesquilé", "Sibaté", "Silvania", "Simijaca",
            "Soacha", "Sopó", "Subachoque", "Suesca", "Supatá", "Susa", "Sutatausa", "Tabio",
            "Tausa", "Tena", "Tenjo", "Tibacuy", "Tibirita", "Tocaima", "Tocancipá", "Topaipí",
            "Ubalá", "Ubaque", "Ubaté", "Une", "Útica", "Venecia", "Vergara", "Vianí",
            "Villagómez", "Villapinzón", "Villeta", "Viotá", "Yacopí", "Zipacón", "Zipaquirá"
        ));

        departamentos.put("27", "Chocó");
        municipiosPorDepartamento.put("27", Arrays.asList(
            "Quibdó", "Acandí", "Alto Baudó", "Atrato", "Bagadó", "Bahía Solano",
            "Bajo Baudó", "Bojayá", "Cantón de San Pablo", "Cértegui", "Condoto",
            "El Carmen de Atrato", "El Carmen del Darién", "El Litoral del San Juan",
            "Istmina", "Juradó", "Lloró", "Medio Atrato", "Medio Baudó", "Medio San Juan",
            "Nóvita", "Nuquí", "Río Iró", "Río Quito", "Riosucio", "San José del Palmar",
            "Sipí", "Tadó", "Unguía", "Unión Panamericana"
        ));

        departamentos.put("41", "Huila");
        municipiosPorDepartamento.put("41", Arrays.asList(
            "Neiva", "Acevedo", "Agrado", "Aipe", "Algeciras", "Altamira", "Baraya",
            "Campoalegre", "Colombia", "Elías", "Garzón", "Gigante", "Guadalupe", "Hobo",
            "Íquira", "Isnos", "La Argentina", "La Plata", "Nátaga", "Oporapa", "Paicol",
            "Palermo", "Palestina", "Pital", "Pitalito", "Rivera", "Saladoblanco",
            "San Agustín", "Santa María", "Suaza", "Tarqui", "Tello", "Teruel", "Tesalia",
            "Tímana", "Villavieja", "Yaguará"
        ));

        departamentos.put("44", "La Guajira");
        municipiosPorDepartamento.put("44", Arrays.asList(
            "Riohacha", "Albania", "Barrancas", "Dibulla", "Distracción", "El Molino",
            "Fonseca", "Hatonuevo", "La Jagua del Pilar", "Maicao", "Manaure", "San Juan del Cesar",
            "Uribia", "Urumita", "Villanueva"
        ));

        departamentos.put("47", "Magdalena");
        municipiosPorDepartamento.put("47", Arrays.asList(
            "Santa Marta", "Algarrobo", "Aracataca", "Ariguaní", "Cerro de San Antonio",
            "Chivolo", "Ciénaga", "Concordia", "El Banco", "El Piñón", "El Retén",
            "Fundación", "Guamal", "Nueva Granada", "Pedraza", "Pijiño del Carmen",
            "Pivijay", "Plato", "Puebloviejo", "Remolino", "Sabanas de San Ángel",
            "Salamina", "San Sebastián de Buenavista", "San Zenón", "Santa Ana",
            "Santa Bárbara de Pinto", "Sitionuevo", "Tenerife", "Zapayán", "Zona Bananera"
        ));

        departamentos.put("50", "Meta");
        municipiosPorDepartamento.put("50", Arrays.asList(
            "Villavicencio", "Acacías", "Barranca de Upía", "Cabuyaro", "Castilla la Nueva",
            "Cubarral", "Cumaral", "El Calvario", "El Castillo", "El Dorado", "Fuente de Oro",
            "Granada", "Guamal", "La Macarena", "La Uribe", "Lejanías", "Mapiripán",
            "Mesetas", "Puerto Concordia", "Puerto Gaitán", "Puerto Lleras", "Puerto López",
            "Puerto Rico", "Restrepo", "San Carlos de Guaroa", "San Juan de Arama",
            "San Juanito", "San Martín", "Vista Hermosa"
        ));

        departamentos.put("52", "Nariño");
        municipiosPorDepartamento.put("52", Arrays.asList(
            "Pasto", "Albán", "Aldana", "Ancuyá", "Arboleda", "Barbacoas", "Belén",
            "Buesaco", "Chachagüí", "Colón", "Consacá", "Contadero", "Córdoba", "Cuaspud",
            "Cumbal", "Cumbitara", "El Charco", "El Peñol", "El Rosario", "El Tablón de Gómez",
            "El Tambo", "Francisco Pizarro", "Funes", "Guachucal", "Guaitarilla", "Gualmatán",
            "Iles", "Imués", "Ipiales", "La Cruz", "La Florida", "La Llanada", "La Tola",
            "La Unión", "Leiva", "Linares", "Los Andes", "Magüí", "Mallama", "Mosquera",
            "Nariño", "Olaya Herrera", "Ospina", "Pizarro", "Policarpa", "Potosí", "Providencia",
            "Puerres", "Pupiales", "Ricaurte", "Roberto Payán", "Samaniego", "San Andrés de Tumaco",
            "San Bernardo", "San José de Albán", "San Lorenzo", "San Pablo", "San Pedro de Cartago",
            "Sandoná", "Santa Bárbara", "Santacruz", "Sapuyes", "Taminango", "Tangua",
            "Túquerres", "Yacuanquer"
        ));

        departamentos.put("54", "Norte de Santander");
        municipiosPorDepartamento.put("54", Arrays.asList(
            "Cúcuta", "Ábrego", "Arboledas", "Bochalema", "Bucarasica", "Cácota",
            "Cachirá", "Chinácota", "Chitagá", "Convención", "Cucutilla", "Durania",
            "El Carmen", "El Tarra", "El Zulia", "Gramalote", "Hacarí", "Herrán",
            "La Esperanza", "La Playa", "Labateca", "Los Patios", "Lourdes", "Mutiscua",
            "Ocaña", "Pamplona", "Pamplonita", "Puerto Santander", "Ragonvalia", "Salazar",
            "San Calixto", "San Cayetano", "Santiago", "Sardinata", "Silos", "Teorama",
            "Tibú", "Toledo", "Villa Caro", "Villa del Rosario"
        ));

        departamentos.put("63", "Quindío");
        municipiosPorDepartamento.put("63", Arrays.asList(
            "Armenia", "Buenavista", "Calarcá", "Circasia", "Córdoba", "Filandia",
            "Génova", "La Tebaida", "Montenegro", "Pijao", "Quimbaya", "Salento"
        ));

        departamentos.put("66", "Risaralda");
        municipiosPorDepartamento.put("66", Arrays.asList(
            "Pereira", "Apía", "Balboa", "Belén de Umbría", "Dosquebradas", "Guática",
            "La Celia", "La Virginia", "Marsella", "Mistrató", "Pueblo Rico", "Quinchía",
            "Santa Rosa de Cabal", "Santuario"
        ));

        departamentos.put("68", "Santander");
        municipiosPorDepartamento.put("68", Arrays.asList(
            "Bucaramanga", "Aguada", "Albania", "Aratoca", "Barbosa", "Barichara", "Barrancabermeja",
            "Betulia", "Bolívar", "Cabrera", "California", "Capitanejo", "Carcasí", "Cepitá",
            "Cerrito", "Charalá", "Charta", "Chipatá", "Cimitarra", "Concepción", "Confines",
            "Contratación", "Coromoro", "Curití", "El Carmen de Chucurí", "El Guacamayo",
            "El Peñón", "El Playón", "Encino", "Enciso", "Florián", "Floridablanca", "Galán",
            "Gámbita", "Girón", "Guaca", "Guadalupe", "Guapotá", "Guavatá", "Güepsa",
            "Hato", "Jesús María", "Jordán", "La Belleza", "La Paz", "Landázuri", "Lebrija",
            "Los Santos", "Macaravita", "Málaga", "Matanza", "Mogotes", "Molagavita",
            "Ocamonte", "Oiba", "Onzaga", "Palmar", "Palmas del Socorro", "Páramo",
            "Piedecuesta", "Pinchote", "Puente Nacional", "Puerto Parra", "Puerto Wilches",
            "Rionegro", "Sabana de Torres", "San Andrés", "San Benito", "San Gil", "San Joaquín",
            "San José de Miranda", "San Miguel", "San Vicente de Chucurí", "Santa Bárbara",
            "Santa Helena del Opón", "Simacota", "Socorro", "Suaita", "Sucre", "Suratá",
            "Tona", "Valle de San José", "Vélez", "Vetas", "Villanueva", "Zapatoca"
        ));

        departamentos.put("70", "Sucre");
        municipiosPorDepartamento.put("70", Arrays.asList(
            "Sincelejo", "Buenavista", "Caimito", "Chalán", "Colosó", "Corozal", "Coveñas",
            "El Roble", "Galeras", "Guaranda", "La Unión", "Los Palmitos", "Majagual",
            "Morroa", "Ovejas", "Palmito", "Sampués", "San Benito Abad", "San Juan de Betulia",
            "San Marcos", "San Onofre", "San Pedro", "Sincé", "Sucre", "Tolú", "Tolú Viejo"
        ));

        departamentos.put("73", "Tolima");
        municipiosPorDepartamento.put("73", Arrays.asList(
            "Ibagué", "Alpujarra", "Alvarado", "Ambalema", "Anzoátegui", "Armero Guayabal",
            "Ataco", "Cajamarca", "Carmen de Apicalá", "Casabianca", "Chaparral", "Coello",
            "Coyaima", "Cunday", "Dolores", "Espinal", "Falan", "Flandes", "Fresno", "Guamo",
            "Herveo", "Honda", "Icononzo", "Lérida", "Líbano", "Mariquita", "Melgar",
            "Murillo", "Natagaima", "Ortega", "Palocabildo", "Piedras", "Planadas", "Prado",
            "Purificación", "Rioblanco", "Roncesvalles", "Rovira", "Saldaña", "San Antonio",
            "San Luis", "Santa Isabel", "Suárez", "Valle de San Juan", "Venadillo",
            "Villahermosa", "Villarrica"
        ));

        departamentos.put("76", "Valle del Cauca");
        municipiosPorDepartamento.put("76", Arrays.asList(
            "Cali", "Alcalá", "Andalucía", "Ansermanuevo", "Argelia", "Bolívar", "Buenaventura",
            "Buga", "Bugalagrande", "Caidedonia", "Calima", "Candelaria", "Cartago", "Dagua",
            "El Águila", "El Cairo", "El Cerrito", "El Dovio", "Florida", "Ginebra", "Guacarí",
            "Jamundí", "La Cumbre", "La Unión", "La Victoria", "Obando", "Palmira", "Pradera",
            "Restrepo", "Riofrío", "Roldanillo", "San Pedro", "Sevilla", "Toro", "Trujillo",
            "Tuluá", "Ulloa", "Versalles", "Vijes", "Yotoco", "Yumbo", "Zarzal"
        ));

        departamentos.put("81", "Arauca");
        municipiosPorDepartamento.put("81", Arrays.asList(
            "Arauca", "Arauquita", "Cravo Norte", "Fortul", "Puerto Rondón", "Saravena", "Tame"
        ));

        departamentos.put("85", "Córdoba");
        municipiosPorDepartamento.put("85", Arrays.asList(
            "Montería", "Ayapel", "Buenavista", "Canalete", "Cereté", "Chimá", "Chinú",
            "Ciénaga de Oro", "Cotorra", "La Apartada", "Lorica", "Los Córdobas", "Momil",
            "Moñitos", "Planeta Rica", "Pueblo Nuevo", "Puerto Escondido", "Puerto Libertador",
            "Purísima", "Sahagún", "San Andrés de Sotavento", "San Antero", "San Bernardo del Viento",
            "San Carlos", "San José de Uré", "San Pelayo", "Tierralta", "Tuchín", "Valencia"
        ));

        departamentos.put("86", "Putumayo");
        municipiosPorDepartamento.put("86", Arrays.asList(
            "Mocoa", "Colón", "Orito", "Puerto Asís", "Puerto Caicedo", "Puerto Guzmán",
            "Puerto Leguízamo", "San Francisco", "San Miguel", "Santiago", "Sibundoy",
            "Valle del Guamuez", "Villagarzón"
        ));

        departamentos.put("88", "San Andrés y Providencia");
        municipiosPorDepartamento.put("88", Arrays.asList("San Andrés", "Providencia"));

        departamentos.put("91", "Amazonas");
        municipiosPorDepartamento.put("91", Arrays.asList("Leticia", "Puerto Nariño"));

        departamentos.put("94", "Guainía");
        municipiosPorDepartamento.put("94", Arrays.asList("Inírida", "Barranco Minas", "Cacahual", "Pana Pana", "San Felipe", "Puerto Colombia", "La Guadalupe", "Mapiripana", "Morichal"));

        departamentos.put("95", "Vaupés");
        municipiosPorDepartamento.put("95", Arrays.asList("Mitú", "Caruru", "Pacoa", "Papunaua", "Taraira", "Yavaraté"));

        departamentos.put("97", "Vichada");
        municipiosPorDepartamento.put("97", Arrays.asList("Puerto Carreño", "Cumaribo", "La Primavera", "Santa Rosalía"));
    }

    public List<DepartamentoDTO> listarDepartamentos() {
        return departamentos.entrySet().stream()
            .map(e -> new DepartamentoDTO(e.getKey(), e.getValue()))
            .sorted(Comparator.comparing(DepartamentoDTO::nombre))
            .collect(Collectors.toList());
    }

    public List<MunicipioDTO> listarMunicipios(String codigoDepartamento) {
        if (codigoDepartamento == null || codigoDepartamento.isBlank()) {
            return Collections.emptyList();
        }

        String normalizedCode = codigoDepartamento.trim();

        if (normalizedCode.startsWith("0")) {
            normalizedCode = normalizedCode.substring(1);
        }

        List<String> municipios = municipiosPorDepartamento.get(normalizedCode);

        if (municipios == null) {
            return Collections.emptyList();
        }

        return municipios.stream()
            .map(nombre -> new MunicipioDTO(generarCodigoMunicipio(nombre), nombre, codigoDepartamento))
            .sorted(Comparator.comparing(MunicipioDTO::nombre))
            .collect(Collectors.toList());
    }

    private String generarCodigoMunicipio(String nombre) {
        if (nombre == null || nombre.length() < 3) {
            return nombre != null ? nombre.toUpperCase() : "";
        }
        return nombre.substring(0, 3).toUpperCase().replaceAll("[^A-Z]", "");
    }
}