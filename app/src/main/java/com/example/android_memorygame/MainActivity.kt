package com.example.android_memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.example.android_memorygame.R.drawable.*

private const val TAG = "MainActivity";
class MainActivity : AppCompatActivity() {
    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)

        val images = mutableListOf(cardimage1, cardimage2, cardimage3, cardimage4, cardimage5)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(imageButton1, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6, imageButton7, imageButton8, imageButton9, imageButton10)

        cards = buttons.indices.map {index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed{ index, imageButton ->
            imageButton.setOnClickListener{
                Log.i(TAG, "¡El botón ha sido presionado!")
                updateModels(index)
                updateViews()
            }
        }
    }

    private fun updateViews() {
        cards.forEachIndexed{index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.huevito)
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        // Error checking
        if (card.isFaceUp){
            Toast.makeText(this, "¡Movimiento inválido!", Toast.LENGTH_SHORT).show()
            return
        }

        if (indexOfSingleSelectedCard == null){
            // 0 o 2 cartas seleccionadas previamente
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // 1 carta seleccionada previamente
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for(card in cards){
            if (!card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier){
            Toast.makeText(this, "¡Has encontrado un par!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
        }
    }
}