package com.rach.co.homescreen.presentation.Screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.rach.co.ad.BannerAdView
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.presentation.viewmodel.NotesViewModel


@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {

    // yaha folder name direct pass kar diya
    LaunchedEffect(Unit) {
        viewModel.viewAllNotes()
    }

    var query by remember { mutableStateOf("") }
    val allPdfs = viewModel.notesPdf.collectAsState().value
    val searchPdfs = viewModel.pdfSearch.collectAsState().value

    val pdfs = if (query.isNotEmpty() && searchPdfs.isNotEmpty()) {
        searchPdfs
    } else {
        allPdfs
    }


//    Log.d("SEARCH",searchData.firstOrNull()!!.chapterName.toString())

Column(  modifier = Modifier
    .fillMaxSize()
    .statusBarsPadding()
) {
    PdfSearchBar(query, onValueChange = {
        query=it
    }, onSearchClick = {
        if (query.isNotEmpty()) {
            viewModel.pdfSearchBar(query)
            viewModel.viewAllNotes()
        } else {
            viewModel.viewAllNotes()
        }


    }

    )
    LazyVerticalGrid(
        modifier = Modifier.weight(1f),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        items(pdfs) { item ->

            PdfCard(navController, item)

        }

    }
}
}


@Composable
fun PdfSearchBar(
    query: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {


    OutlinedTextField(value = query, onValueChange =  onValueChange, placeholder = {
        Text(text = "Search notes ......")},
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        leadingIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }, singleLine = true,
        shape = RoundedCornerShape(15.dp)
    )



}

// PDF card
@Composable
fun PdfCard(
    navController: NavController,
    item: NotesItems
) {

    val uri =
        Uri.encode(item.pdflinkchapterName ?: "")



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .clickable {

                navController.navigate(
                    "pdfscreen/$uri"
                )

            },

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Image(
            painter =
                painterResource(id = R.drawable.pdf),

            contentDescription = "",

            modifier =
                Modifier.size(80.dp)
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        item.chapterName?.let {
            Text(

                text =
                    it,

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }

    }

}