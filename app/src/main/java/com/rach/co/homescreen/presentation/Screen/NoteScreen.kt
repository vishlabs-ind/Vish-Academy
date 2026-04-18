package com.rach.co.homescreen.presentation.Screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

    val pdfs =
        viewModel.notesPdf.collectAsState().value


    LazyVerticalGrid(
        modifier = Modifier.statusBarsPadding(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(pdfs) { item ->

            PdfCard(navController, item)

        }

    }
}


// PDF card
@Composable
fun PdfCard(
    navController: NavController,
    item: NotesItems
) {

    val uri =
        Uri.encode(item.pdflinkchapterName ?: "")

    Log.d("PDF_LINK", item.pdflinkchapterName ?: "")

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