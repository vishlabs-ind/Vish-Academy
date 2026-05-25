package com.rach.co.homescreen.presentation.Screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rach.co.R
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.presentation.viewmodel.NotesViewModel

@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.viewAllNotes()
    }

    var query by remember { mutableStateOf("") }

    val allPdfs = viewModel.notesPdf.collectAsState().value
    val searchPdfs = viewModel.pdfSearch.collectAsState().value


    val pdfs = if (query.isNotEmpty()) searchPdfs else allPdfs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        PdfSearchBar(
            query = query,
            onValueChange = {
                query = it
                viewModel.pdfSearchBar(it)
            },
            onSearchClick = {
                viewModel.pdfSearchBar(query)
            },
            onClearClick = {
                query = ""
                viewModel.viewAllNotes()
            }
        )


        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            if (pdfs.isEmpty()) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.pdf),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (query.isNotEmpty())
                            "No results found for \"$query\""
                        else
                            "No data available",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    itemsIndexed(pdfs) { index, item ->

                        PdfCard(navController, item)

                        if (index == pdfs.lastIndex) {

                            LaunchedEffect(index) {
                                viewModel.loadMoreNotes()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PdfSearchBar(
    query: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit
) {

    OutlinedTextField(
        value = query,
        onValueChange = onValueChange,
        placeholder = { Text("Search notes ......") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),

        leadingIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        },

        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        },

        singleLine = true,
        shape = RoundedCornerShape(15.dp)
    )
}

@Composable
fun PdfCard(
    navController: NavController,
    item: NotesItems
) {

    val uri = Uri.encode(item.pdflinkchapterName ?: "")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .clickable {
                navController.navigate("pdfscreen/$uri")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.pdf),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        item.chapterName?.let {
            Text(
                text = it,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}