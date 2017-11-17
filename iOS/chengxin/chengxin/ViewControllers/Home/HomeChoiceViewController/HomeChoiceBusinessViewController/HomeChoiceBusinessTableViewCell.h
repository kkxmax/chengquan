//
//  HomeChoiceBusinessTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeChoiceBusinessTableViewCell : UITableViewCell
{
}
@property (nonatomic, strong) UITableView *contentTableView;
@property (nonatomic, strong) IBOutlet UILabel *networkNameLabel;
@property (nonatomic, strong) IBOutlet UIButton *extendButton;
@property (nonatomic, strong) IBOutlet UIView *networkContentView;
@property (nonatomic, strong) IBOutlet UIButton *networkContentButton;
@property (nonatomic, strong) IBOutlet UIButton *leftButton;
@property (nonatomic, strong) IBOutlet UIButton *centerButton;
@property (nonatomic, strong) IBOutlet UIButton *rightButton;
@end
