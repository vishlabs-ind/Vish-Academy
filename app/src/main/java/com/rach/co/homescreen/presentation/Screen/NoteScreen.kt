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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import com.rach.co.homescreen.presentation.viewmodel.NotesViewModel
import com.rach.co.R
import com.rach.co.homescreen.data.DataClass.NotesItems
import okhttp3.Route


//main Notes screen
@Composable
fun NoteScreen(navController: NavHostController) {

    val notes = listOf("Hindi PYQ", "Maths Youtube 1")

    LazyVerticalGrid(
        modifier = Modifier.statusBarsPadding(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(notes) { item ->
            PdfFolder( item,navController)
        }
    }
}

// All folder in main screen are showing here
@Composable
fun PdfFolder(item: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .clickable {
                navController.navigate("allPdfs/$item")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.folder),
            contentDescription = "",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// After click on folder show the screen where all pdf all available
@Composable
fun PdfsScreen(name:String?,navController: NavController, viewModel: NotesViewModel = hiltViewModel()) {

    LaunchedEffect(Unit)
    {
        if(name =="Hindi PYQ")
        {
            viewModel.viewHindiNotes()

        }
        else
        {
            viewModel.viewNotes()
        }
    }
    //fatch hindi pdf
    val hindipdfs = viewModel.notesPdfHindi.collectAsState().value

//fatch math pdf
    val pdfs = viewModel.notesPdf.collectAsState().value
    val data =
        if (name=="Hindi PYQ"){
            hindipdfs
        }else{
            pdfs
        }

    PdfsShow(data,navController)


}



@Composable
fun PdfsShow(pdfs: List<NotesItems>, navController: NavController) {
    LazyVerticalGrid(modifier = Modifier.statusBarsPadding(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pdfs) { item ->
            PdfCard(navController, item)
        }
    }
}

//pdf card
@Composable
fun PdfCard(navController: NavController, item: NotesItems) {
    val uri=Uri.encode(item.pdflinkchapterName)

    Log.d("TAGE_NOTE",item.chapterName.toString())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .clickable {

                navController.navigate("pdfscreen/$uri") //
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.pdf),
            contentDescription = "",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.chapterName .toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}




