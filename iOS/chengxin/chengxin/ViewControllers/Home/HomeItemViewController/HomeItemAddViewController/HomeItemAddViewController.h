//
//  HomeItemAddViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ImageChooseViewController.h"

@interface HomeItemAddViewController : UIViewController<UIPickerViewDelegate, UIPickerViewDataSource, UITextViewDelegate, UITextFieldDelegate, ImageChooseViewControllerDelegate>
{
}
@property (nonatomic, strong) NSString *changedItemID;
@property (nonatomic, strong) NSDictionary *item;
@property (nonatomic, strong) IBOutlet UIImageView *logoImageView;
@property (nonatomic, strong) IBOutlet UILabel *logoImageLabel;
@property (nonatomic, strong) IBOutlet UIPickerView *cityPickerView;
@property (nonatomic, strong) IBOutlet UIView *citySelectView;
@property (nonatomic, strong) IBOutlet UIButton *citySelectButton;
@property (nonatomic, strong) IBOutlet UIView *transView;
@property (nonatomic, strong) IBOutlet UILabel *categoryLabel;
@property (nonatomic, strong) IBOutlet UITextView *itemCommentTextView;
@property (nonatomic, strong) IBOutlet UILabel *itemCommentLimitLabel;
@property (nonatomic, strong) IBOutlet UITextField *needTextField;
@property (nonatomic, strong) IBOutlet UITextField *weburlTextField;
@property (nonatomic, strong) IBOutlet UITextField *nameTextField;
@property (nonatomic, strong) IBOutlet UIButton *isShowButton;
@property (nonatomic, strong) IBOutlet UITextField *contactNameTextField;
@property (nonatomic, strong) IBOutlet UITextField *contactMobileTextField;
@property (nonatomic, strong) IBOutlet UITextField *contactWeixinTextField;
@property (nonatomic, strong) IBOutlet UIView *cityNoSelectView;
@property (nonatomic, strong) IBOutlet UILabel *cityLabel;
@property (nonatomic, strong) IBOutlet UILabel *navTitleLabel;
@property (nonatomic, strong) IBOutlet UITextField *addressDetailTextField;

@property (nonatomic, strong) IBOutlet UILabel *nameTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *categoryTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *cityTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *contentTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *reasonTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *networkTitleLabel;

@end
