//
//  Global.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#ifndef Global_h
#define Global_h

#define appDelegate ((AppDelegate *)[[UIApplication sharedApplication]delegate])

// SCREEN SIZE
#define SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
#define SCREEN_HEIGHT ([[UIScreen mainScreen] bounds].size.height)
#define SCREEN_MAX_LENGTH (MAX(SCREEN_WIDTH, SCREEN_HEIGHT))
#define SCREEN_MIN_LENGTH (MIN(SCREEN_WIDTH, SCREEN_HEIGHT))

// DEVICE INFO
#define IS_IPAD (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_RETINA ([[UIScreen mainScreen] scale] >= 2.0)
#define IS_IPHONE_4_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 568.0)
#define IS_IPHONE_5 (IS_IPHONE && SCREEN_MAX_LENGTH == 568.0)
#define IS_IPHONE_5_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 667.0)

#define IS_IPHONE_6 (IS_IPHONE && SCREEN_MAX_LENGTH == 667.0)
#define IS_IPHONE_6P (IS_IPHONE && SCREEN_MAX_LENGTH == 736.0)

// COLOR
#define ORANGE_COLOR   [UIColor colorWithRed:255/255.0f green:138/255.0f blue:0/255.0f alpha:1.0]
#define BLACK_COLOR_51   [UIColor colorWithRed:51/255.0f green:51/255.0f blue:51/255.0f alpha:1.0]
#define BLACK_COLOR_85   [UIColor colorWithRed:85/255.0f green:85/255.0f blue:85/255.0f alpha:1.0]
#define BLACK_COLOR_102   [UIColor colorWithRed:102/255.0f green:102/255.0f blue:102/255.0f alpha:1.0]
#define BLACK_COLOR_153   [UIColor colorWithRed:153/255.0f green:153/255.0f blue:153/255.0f alpha:1.0]
#define BLACK_COLOR_245   [UIColor colorWithRed:245/255.0f green:245/255.0f blue:245/255.0f alpha:1.0]
#define RGB_COLOR(r, g, b) [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:1.0]
#define RGBA_COLOR(r, g, b, a) [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:a/255.0f]

// Home
#define NUMBER_OF_SLIDE_IMAGES          4
#define FAMILIAR_IMAGE_START            1090
#define ENTERPRISE_IMAGE_START          1095
#define COMMERCE_IMAGE_START            1100
#define ITEM_IMAGE_START                1095
#define SERVICE_IMAGE_START             1100
#define SLIDE_SECOND                    3

// Round Button
#define ROUNDBUTTONRADIUS                   4.f
#define SELECTED_BUTTON_BACKGROUND_COLOR    RGB_COLOR(209, 233, 250)
#define SELECTED_BUTTON_TITLE_COLOR         RGB_COLOR(23, 133, 229)

// City Array
#define CityArray ([[NSArray alloc] initWithObjects:@"北京",@"上海",@"广州",@"深圳", nil])

// Table Section
#define AlphabetsArray ([[NSArray alloc] initWithObjects:@"A",@"B",@"C",@"D",@"E",@"F",@"G",@"H",@"I",@"J",@"K",@"L",@"M",@"N",@"O",@"P",@"Q",@"R",@"S",@"T",@"U",@"V",@"W",@"X",@"Y",@"Z", nil])

// FONT
#define FONT_12    [UIFont fontWithName:@"Helvetica Neue" size:12.0f]
#define FONT_16    [UIFont fontWithName:@"Helvetica Neue" size:16.0f]

// Notification Name
#define ACTIVITY_TRANS_TAB_NOTIFICATION         @"ActivityTransparencyTabNotification"
#define SHOW_HOMECHOICE_VIEW_NOTIFICATION       @"ShowHomeChoiceViewNotification"
#define HIDE_HOMECHOICE_VIEW_NOTIFICATION       @"HideHomeChoiceViewNotification"
#define SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION   @"ShowHomeItemDetailViewNotification"

#endif /* Global_h */
