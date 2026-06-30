package com.example.test_1_project

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.test_1_project.data.Coffee
import com.example.test_1_project.data.coffeeList
import com.example.test_1_project.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeScreen(cartViewModel: CartViewModel, favoritesViewModel: FavoritesViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredCoffeeList = remember(searchQuery) {
        if (searchQuery.isEmpty()) coffeeList
        else coffeeList.filter { it.name.contains(searchQuery, ignoreCase = true) || it.type.contains(searchQuery, ignoreCase = true) }
    }

    val pagerState = rememberPagerState(pageCount = { filteredCoffeeList.size })
    val currentCoffee = if (filteredCoffeeList.isNotEmpty()) filteredCoffeeList[pagerState.currentPage] else coffeeList[0]
    
    val animatedBgColor by animateColorAsState(
        targetValue = currentCoffee.backgroundColor,
        animationSpec = tween(durationMillis = 600),
        label = "bgColor"
    )

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            if (selectedTab == 0) {
                Column(
                    modifier = Modifier
                        .background(animatedBgColor)
                        .statusBarsPadding(), // Ensures content is below the status bar/notch
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "Caféora",
                                color = currentCoffee.secondaryColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(
                                    Icons.Default.Notifications, 
                                    contentDescription = null, 
                                    tint = currentCoffee.secondaryColor
                                )
                            }
                        },
                        windowInsets = WindowInsets(0, 0, 0, 0) // We handle insets in the parent Column
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp)) // Added space to prevent overlap
                    
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        placeholder = { 
                            Text(
                                "Search your coffee...", 
                                color = Color.Black.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            ) 
                        },
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Search, 
                                contentDescription = null, 
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            ) 
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(52.dp), // Slightly increased for better tap target
                        colors = SearchBarDefaults.colors(
                            containerColor = Color.White,
                            inputFieldColors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedPlaceholderColor = Color.Black.copy(alpha = 0.5f),
                                unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(30.dp),
                        tonalElevation = 8.dp,
                        shadowElevation = 4.dp
                    ) { }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = if (selectedTab == 0) animatedBgColor else DarkBrown,
                contentColor = if (selectedTab == 0) currentCoffee.secondaryColor else Cream
            ) {
                val items = listOf(
                    Triple(0, Icons.Default.Home, "Home"),
                    Triple(1, Icons.Default.Favorite, "Favorites"),
                    Triple(2, Icons.Default.ShoppingCart, "Cart"),
                    Triple(3, Icons.Default.Person, "Profile")
                )
                
                items.forEach { (index, icon, label) ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = { 
                            BadgedBox(badge = {
                                if (index == 2 && cartViewModel.cartItems.isNotEmpty()) {
                                    Badge { Text(cartViewModel.cartItems.size.toString()) }
                                }
                            }) {
                                Icon(icon, contentDescription = null)
                            }
                        },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (selectedTab == 0) currentCoffee.backgroundColor else DarkBrown,
                            selectedTextColor = if (selectedTab == 0) currentCoffee.secondaryColor else Cream,
                            indicatorColor = if (selectedTab == 0) currentCoffee.secondaryColor else Cream,
                            unselectedIconColor = (if (selectedTab == 0) currentCoffee.secondaryColor else Cream).copy(alpha = 0.6f),
                            unselectedTextColor = (if (selectedTab == 0) currentCoffee.secondaryColor else Cream).copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (selectedTab == 0) animatedBgColor else DarkBrown)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    if (filteredCoffeeList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No coffee found", color = currentCoffee.secondaryColor)
                        }
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) { page ->
                            CoffeePage(coffee = filteredCoffeeList[page], cartViewModel = cartViewModel, favoritesViewModel = favoritesViewModel)
                        }
                    }

                    // Page Indicator removed as per user request
                }
                1 -> FavoritesScreen(favoritesViewModel, cartViewModel)
                2 -> CartScreen(cartViewModel) { selectedTab = 0 }
                3 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun CoffeePage(coffee: Coffee, cartViewModel: CartViewModel, favoritesViewModel: FavoritesViewModel) {
    var quantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()
    val isFavorite = favoritesViewModel.isFavorite(coffee.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(coffee.secondaryColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(coffee.imageUrl),
                contentDescription = coffee.name,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    favoritesViewModel.toggleFavorite(coffee)
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(coffee.secondaryColor.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color(0xFFFF4081) else coffee.secondaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = coffee.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = coffee.secondaryColor
        )
        
        Text(
            text = coffee.type,
            fontSize = 16.sp,
            color = coffee.secondaryColor.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quantity Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = { if (quantity > 1) quantity-- },
                modifier = Modifier
                    .size(36.dp)
                    .background(coffee.secondaryColor.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = coffee.secondaryColor, modifier = Modifier.size(20.dp))
            }
            
            Text(
                text = quantity.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = coffee.secondaryColor
            )

            IconButton(
                onClick = { quantity++ },
                modifier = Modifier
                    .size(36.dp)
                    .background(coffee.secondaryColor.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = coffee.secondaryColor, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$${String.format("%.2f", coffee.price * quantity)}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = coffee.secondaryColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = coffee.description,
            fontSize = 14.sp,
            color = coffee.secondaryColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 24.dp),
            lineHeight = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { cartViewModel.addToCart(coffee, quantity) },
            colors = ButtonDefaults.buttonColors(
                containerColor = coffee.secondaryColor,
                contentColor = coffee.backgroundColor
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 32.dp)
        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Buy Now", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}
