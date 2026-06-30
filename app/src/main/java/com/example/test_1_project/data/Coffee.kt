package com.example.test_1_project.data

import androidx.compose.ui.graphics.Color
import com.example.test_1_project.ui.theme.*

data class Coffee(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageBase64: String = "",
    val imageUrl: String = "",
    val backgroundColor: Color = Chocolate,
    val secondaryColor: Color = Cream
)

val coffeeList = listOf(
    Coffee(
        id = "1",
        name = "Espresso",
        type = "Strong & Bold",
        price = 3.50,
        description = "Pure and intense coffee experience.",
        imageUrl = "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFF3E2723), 
        secondaryColor = Color(0xFFFFFDD0)
    ),
    Coffee(
        id = "2",
        name = "Strawberry Smoothy",
        type = "Fresh & Fruity",
        price = 5.50,
        description = "Refreshing and sweet strawberry blend.",
        imageUrl = "https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFFF06292),
        secondaryColor = Color(0xFFFFFDD0)
    ),
    Coffee(
        id = "3",
        name = "Milk Americano",
        type = "Smooth Balance",
        price = 4.00,
        description = "Classic espresso with a splash of milk.",
        imageUrl = "https://images.unsplash.com/photo-1509042239860-f550ce710b93?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFF795548),
        secondaryColor = Color(0xFFFFFDD0)
    ),
    Coffee(
        id = "4",
        name = "Cappuccino",
        type = "Creamy Foam",
        price = 4.50,
        description = "Rich espresso with velvety steamed milk foam.",
        imageUrl = "https://images.unsplash.com/photo-1534778101976-62847782c213?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFF8D6E63),
        secondaryColor = Color(0xFFFFFDD0)
    ),
    Coffee(
        id = "5",
        name = "Iced Latte",
        type = "Chilled Refreshment",
        price = 4.75,
        description = "Cold espresso balanced with creamy chilled milk.",
        imageUrl = "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFF5D4037),
        secondaryColor = Color(0xFFFFFDD0)
    ),
    Coffee(
        id = "6",
        name = "Matcha Latte",
        type = "Zen Green",
        price = 5.25,
        description = "Premium green tea matcha with smooth milk.",
        imageUrl = "https://images.unsplash.com/photo-1515823064-d6e0c04616a7?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFF81C784),
        secondaryColor = Color(0xFFE8F5E9)
    ),
    Coffee(
        id = "7",
        name = "Caramel Macchiato",
        type = "Sweet Drizzle",
        price = 5.50,
        description = "Espresso with vanilla and sweet caramel drizzle.",
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFFFFA726),
        secondaryColor = Color(0xFFFFFDD0)
    ),
    Coffee(
        id = "8",
        name = "Blueberry Cooler",
        type = "Berry Blast",
        price = 5.00,
        description = "Refreshing iced drink with fresh blueberry notes.",
        imageUrl = "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFF5C6BC0),
        secondaryColor = Color(0xFFE8EAF6)
    ),
    Coffee(
        id = "9",
        name = "Vanilla Frappe",
        type = "Sweet & Icy",
        price = 6.00,
        description = "Blended icy treat with rich vanilla flavor.",
        imageUrl = "https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=1000&auto=format&fit=crop",
        backgroundColor = Color(0xFFBCAAA4),
        secondaryColor = Color(0xFF4B3621)
    ),
    Coffee(
        id = "10",
        name = "Dark Mocha",
        type = "Choco Intensity",
        price = 5.25,
        description = "Rich fusion of dark chocolate and bold espresso.",
        imageUrl = "https://t4.ftcdn.net/jpg/06/85/03/65/360_F_685036582_QOdhu9QUVJQ2JdJJz9rhPbVLtPZ7KNNQ.jpg",
        backgroundColor = Color(0xFF2D1B17),
        secondaryColor = Color(0xFFFFFDD0)
    )
)
