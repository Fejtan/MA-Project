package com.project.cardgame.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.project.cardgame.R
import com.project.cardgame.data.model.internal.api.Status
import com.project.cardgame.data.model.internal.game.Card
import com.project.cardgame.data.model.internal.game.Level
import com.project.cardgame.data.model.internal.user.UserData
import com.project.cardgame.ui.ParentFragment
import com.project.cardgame.ui.main.adapters.CardsRVAdapter
import com.project.cardgame.ui.main.viewmodels.GameViewModel
import com.project.cardgame.utils.getGameDialog
import com.project.cardgame.utils.helper.GridSpacingItemDecoration
import com.project.cardgame.utils.toastStringRes
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : ParentFragment()
{

	private val gameViewModel by lazy { ViewModelProviders.of(this).get(GameViewModel::class.java) }

	private lateinit var cardsRvAdapter: CardsRVAdapter

	private var firstFlippedCardWithPosition: Pair<Int, Card>? = null
	private var secondFlippedCardWithPosition: Pair<Int, Card>? = null

	private val gameTimeMs by lazy { resources.getInteger(R.integer.game_time_ms).toLong() }
	private val flippedCardsNoMatchDurationTimeMs by lazy {
		resources.getInteger(R.integer.flipped_cards_no_match_duration_time_ms).toLong()
	}
	private val flippedCardsMatchDurationTimeMs by lazy {
		resources.getInteger(R.integer.flipped_cards_match_duration_time_ms).toLong()
	}

	override fun getLayoutResId() = R.layout.fragment_game

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		observeGameLevel()
		observeRemainingTime()
		observeScore()
		observeSaveUserScoreResult()
		gameViewModel.startGame(gameTimeMs)
	}

	private fun observeGameLevel()
	{
		gameViewModel.currentGameLevel.observe(viewLifecycleOwner, Observer { level ->
			if (level == null)
			{
				gameSuccessfullyFinished(
					score = gameViewModel.score.value ?: 0,
					remainingTimeMs = gameViewModel.remainingTimeMs.value ?: 0L
				)
				return@Observer
			}
			if (level.level == 1)
			{
				startLevel(level)
			}
			else
			{
				levelFinished(
					nextLevel = level,
					score = gameViewModel.score.value ?: 0,
					remainingTimeMs = gameViewModel.remainingTimeMs.value ?: 0L
				)
			}
		})
	}

	private fun observeRemainingTime()
	{
		gameViewModel.remainingTimeMs.observe(viewLifecycleOwner, { remainingTimeMs ->
			val remainingTimeMinSec = getTimeMinSec(remainingTimeMs)
			tvTime.text = getString(R.string.gameTime, remainingTimeMinSec.first, remainingTimeMinSec.second)
			if (remainingTimeMs == 0L) {
				// Time is over
				gameOver(score = gameViewModel.score.value ?: 0)
			}
		})
	}

	private fun observeScore()
	{
		gameViewModel.score.observe(viewLifecycleOwner, { score ->
			tvScore.text = getString(R.string.gameScore, score)
		})
	}

	private fun observeSaveUserScoreResult()
	{
		gameViewModel.saveUserScoreResult.observe(viewLifecycleOwner, { result ->
			when (result.status)
			{
				Status.SUCCESS -> {
					context?.toastStringRes(R.string.scoreSavedToast)
				}
				Status.ERROR -> {
					context?.toastStringRes(R.string.scoreSaveFailedToast)
				}
				Status.LOADING -> {
				}
			}
		})
	}

	private fun startLevel(level: Level)
	{
		setCardsRecyclerView(level.cards, level.numberOfColumns)
	}

	private fun levelFinished(nextLevel: Level, score: Int, remainingTimeMs: Long)
	{
		gameViewModel.pauseTimer()
		var levelFinishedDialog: AlertDialog? = null
		val oldLevel = nextLevel.level - 1
		val remainingTimeMinSec = getTimeMinSec(remainingTimeMs)
		levelFinishedDialog = context?.getGameDialog(
			title = getString(R.string.gameDialogOnLevelComplete, oldLevel),
			titleColorResId = R.color.black,
			info1 = getString(R.string.gameScore, score),
			info2 = getString(
				R.string.gameDialogTimeRemain,
				remainingTimeMinSec.first,
				remainingTimeMinSec.second
			),
			infoBackgroundColorResId = R.color.black,
			playButtonText = getString(R.string.gameDialogNextLevelButton),
			playButtonClickListener = {
				levelFinishedDialog?.dismiss()
				goNextLevel(nextLevel)
			}
		)?.apply {
			setCancelable(false)
			show()
		}
	}

	private fun goNextLevel(nextLevel: Level)
	{
		gameViewModel.startTimer()
		startLevel(nextLevel)
	}

	private fun gameSuccessfullyFinished(score: Int, remainingTimeMs: Long)
	{
		gameViewModel.pauseTimer()
		gameViewModel.saveUserScore(score)

		var gameFinishedDialog: AlertDialog? = null
		val remainingTimeMinSec = getTimeMinSec(remainingTimeMs)
		gameFinishedDialog = context?.getGameDialog(
			title = getString(R.string.gameDialogWin),
			titleColorResId = R.color.green400,
			info1 = getString(R.string.gameScore, score),
			info2 = getString(
				R.string.gameDialogTimeRemain,
				remainingTimeMinSec.first,
				remainingTimeMinSec.second
			),
			info3 = getString(R.string.gameDialogLastHighScore, UserData.highScore),
			infoBackgroundColorResId = R.color.green800,
			playButtonText = getString(R.string.gameDialogPlayAgainButton),
			playButtonClickListener = {
				gameFinishedDialog?.dismiss()
				playAgain()
			},
			leaderboardButtonText = getString(R.string.gameDialogShareScoreButton),
			leaderboardButtonClickListener = { shareScore(score) }
		)?.apply {
			setOnCancelListener {
				activity?.onBackPressed()
			}
			show()
		}
	}

	private fun gameOver(score: Int)
	{
		gameViewModel.pauseTimer()
		gameViewModel.saveUserScore(score)

		var gameOverDialog: AlertDialog? = null
		gameOverDialog = context?.getGameDialog(
			title = getString(R.string.gameDialogGameOver),
			titleColorResId = R.color.red400,
			info1 = getString(R.string.gameScore, score),
			info2 = getString(R.string.gameDialogLastHighScore, UserData.highScore),
			infoBackgroundColorResId = R.color.red800,
			playButtonText = getString(R.string.gameDialogPlayAgainButton),
			playButtonClickListener = {
				gameOverDialog?.dismiss()
				playAgain()
			},
			leaderboardButtonText = getString(R.string.gameDialogShareScoreButton),
			leaderboardButtonClickListener = { shareScore(score) }
		)?.apply {
			setOnCancelListener {
				activity?.onBackPressed()
			}
			show()
		}
	}

	private fun playAgain()
	{
		gameViewModel.restartGame()
	}

	private fun shareScore(score: Int)
	{
		val appName = getString(R.string.app_name)
		val appUrl = getString(R.string.app_url)
		val sharingText = getString(R.string.scoreShareInfo, score, appName, appUrl)
		val shareIntent = Intent()
		shareIntent.action = Intent.ACTION_SEND
		shareIntent.putExtra(Intent.EXTRA_TEXT, sharingText)
		shareIntent.type = "text/plain"
		startActivity(shareIntent)

	}

	private fun setCardsRecyclerView(cards: List<Card>, numberOfColumns: Int)
	{
		// Calculate cardSizePx
		val rvWidthPx = resources.displayMetrics.widthPixels
		val gameCardsSpacingPx = if (numberOfColumns > 3)
		{
			resources.getDimensionPixelSize(R.dimen.game_cards_small_spacing)
		}
		else
		{
			resources.getDimensionPixelSize(R.dimen.game_cards_large_spacing)
		}

		val totalSpacingWidthPx = ((numberOfColumns + 1) * gameCardsSpacingPx)
		val cardSizePx = (rvWidthPx - totalSpacingWidthPx) / numberOfColumns
		// Set recyclerView
		rvCards.layoutManager = GridLayoutManager(context, numberOfColumns)
		if (rvCards.itemDecorationCount > 0)
		{
			rvCards.removeItemDecorationAt(0)
		}
		rvCards.addItemDecoration(GridSpacingItemDecoration(numberOfColumns, gameCardsSpacingPx, true))
		cardsRvAdapter = CardsRVAdapter(cards, cardSizePx, ::onCardClick)
		rvCards.adapter = cardsRvAdapter
	}

	private fun onCardClick(card: Card, cardPosition: Int)
	{
		if (firstFlippedCardWithPosition != null && secondFlippedCardWithPosition != null) return
		// If the card is facing backwards
		if (!card.isFlippedFront)
		{
			cardsRvAdapter.flipCard(cardPosition)
			if (firstFlippedCardWithPosition == null)
			{
				firstFlippedCardWithPosition = Pair(cardPosition, card)
			}
			else
			{
				secondFlippedCardWithPosition = Pair(cardPosition, card)
				checkCardsForMatch()
			}
		}
	}

	private fun checkCardsForMatch()
	{
		cardsRvAdapter.disableCardsClicks()
		val firstCardWithPosition = firstFlippedCardWithPosition ?: return
		val secondCardWithPosition = secondFlippedCardWithPosition ?: return
		val firstCard = firstCardWithPosition.second
		val firstCardPosition = firstCardWithPosition.first
		val secondCard = secondCardWithPosition.second
		val secondCardPosition = secondCardWithPosition.first

		if (firstCard.drawableResId == secondCard.drawableResId)
		{
			// Cards matched
			Handler().postDelayed({
				cardsRvAdapter.markCardsAsMatchFound(firstCardPosition, secondCardPosition)
				firstFlippedCardWithPosition = null
				secondFlippedCardWithPosition = null
				cardsRvAdapter.enableCardsClicks()
				gameViewModel.matchFound()
			}, flippedCardsMatchDurationTimeMs)
		}
		else
		{
			// No match
			Handler().postDelayed({
				cardsRvAdapter.flipCard(firstCardPosition)
				cardsRvAdapter.flipCard(secondCardPosition)
				firstFlippedCardWithPosition = null
				secondFlippedCardWithPosition = null
				cardsRvAdapter.enableCardsClicks()
			}, flippedCardsNoMatchDurationTimeMs)
		}
	}

	private fun getTimeMinSec(timeMs: Long): Pair<Int, Int>
	{
		val sec = (timeMs / 1000)
		val min = (sec / 60)
		return Pair(min.toInt(), (sec % 60).toInt())
	}

}


